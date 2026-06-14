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


@Component
class InternshipJournalEventHandler(
    private val commandGateway: CommandGateway,
    private val journalEntryReadService: JournalEntryViewReadService
) {

    @EventHandler
    fun handle(event: JournalOpenedEvent) {
        logger.info("Journal opened: ${event.journalId} for student: ${event.studentId}")
    }

    @EventHandler
    fun handle(event: JournalClosedEvent) {
        val draftEntries = journalEntryReadService.findDraftEntriesByJournal(event.journalId)
        
        logger.info("Journal closed: ${event.journalId}. Found ${draftEntries.size} draft entries to auto-reject")

        draftEntries.forEach { entry ->
            val rejectCommand = RejectJournalEntryCommand(
                entryId = entry.journalEntryId,
                rejectedBy = "SYSTEM",
                rejectionReason = "Journal closed - entries cannot remain in draft state"
            )
            
            try {
                @Suppress("UNCHECKED_CAST")
                val result = commandGateway.sendAndWait<Any>(
                    rejectCommand,
                    5,  // timeout
                    TimeUnit.SECONDS
                )
                logger.info("Auto-rejected entry: ${entry.journalEntryId}")
            } catch (e: Exception) {
                logger.error("Failed to auto-reject entry ${entry.journalEntryId}: ${e.message}", e)
            }
        }
        
        logger.info("Finished auto-rejecting draft entries for journal: ${event.journalId}")
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(InternshipJournalEventHandler::class.java)
    }
}

