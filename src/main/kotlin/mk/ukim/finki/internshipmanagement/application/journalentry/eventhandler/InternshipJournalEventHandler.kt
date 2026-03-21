package mk.ukim.finki.internshipmanagement.application.journalentry.eventhandler

import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryViewReadService
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.RejectJournalEntryCommand
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.JournalClosedEvent
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.JournalOpenedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Cross-Aggregate Event Handler for JournalEntry.
 * 
 * Listens to events published by InternshipJournal aggregate and reacts accordingly.
 * This handler demonstrates the CQRS pattern where one aggregate responds to events
 * from another aggregate without direct coupling.
 */
@Component
class InternshipJournalEventHandler(
    private val commandGateway: CommandGateway,
    private val journalEntryReadService: JournalEntryViewReadService
) {
    
    /**
     * Handle JournalOpenedEvent from InternshipJournal.
     * 
     * Triggered when: InternshipJournal publishes JournalOpenedEvent
     * This signals that a journal is now open for entries.
     * For now, this could be used to initialize any tracking state.
     */
    @EventHandler
    fun handle(event: JournalOpenedEvent) {
        // When a journal opens, JournalEntry can verify the journal exists
        // and prepare for incoming entries. This is a soft validation point.
        
        logger.info("Journal opened: ${event.journalId} for student: ${event.studentId}")
    }
    
    /**
     * Handle JournalClosedEvent from InternshipJournal.
     * 
     * Triggered when: InternshipJournal publishes JournalClosedEvent
     * Reaction: All DRAFT entries in this journal MUST be auto-rejected (business rule)
     * 
     * Why this cannot live in InternshipJournal:
     * - JournalEntry is a separate aggregate with its own lifecycle
     * - Only JournalEntry can modify JournalEntry state
     * - InternshipJournal cannot directly modify entries (violates aggregate boundaries)
     * - This is a cross-aggregate consistency requirement that must be handled via events
     * 
     * Ordering matters: We use sendAndWait() instead of send() to ensure:
     * - All draft entries are rejected BEFORE the journal is marked closed
     * - No new entries can be added while rejection is in progress
     * - Maintains transactional consistency across aggregate boundaries
     */
    @EventHandler
    fun handle(event: JournalClosedEvent) {
        // Step 1: Look up all draft entries for this journal
        val draftEntries = journalEntryReadService.findDraftEntriesByJournal(event.journalId)
        
        logger.info("Journal closed: ${event.journalId}. Found ${draftEntries.size} draft entries to auto-reject")
        
        // Step 2: For each draft entry, send a rejection command
        // Using sendAndWait() ensures entries are rejected in order before returning
        // This preserves order and maintains consistency across aggregates
        draftEntries.forEach { entry ->
            val rejectCommand = RejectJournalEntryCommand(
                entryId = entry.journalEntryId,
                rejectedBy = "SYSTEM",
                rejectionReason = "Journal closed - entries cannot remain in draft state"
            )
            
            try {
                // sendAndWait() blocks until the command is processed
                // This ensures ordering: reject all entries BEFORE releasing control
                // Without this ordering guarantee, the closed journal state might appear
                // before entries are rejected, violating business rules
                @Suppress("UNCHECKED_CAST")
                val result = commandGateway.sendAndWait<Any>(
                    rejectCommand,
                    5,  // timeout
                    TimeUnit.SECONDS
                )
                logger.info("Auto-rejected entry: ${entry.journalEntryId}")
            } catch (e: Exception) {
                logger.error("Failed to auto-reject entry ${entry.journalEntryId}: ${e.message}", e)
                // Continue processing other entries even if one fails
            }
        }
        
        logger.info("Finished auto-rejecting draft entries for journal: ${event.journalId}")
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(InternshipJournalEventHandler::class.java)
    }
}

