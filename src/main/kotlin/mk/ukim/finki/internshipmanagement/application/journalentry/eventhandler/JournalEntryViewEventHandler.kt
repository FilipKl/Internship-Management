package mk.ukim.finki.internshipmanagement.application.journalentry.eventhandler

import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryView
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryViewRepository
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.*
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Read Model Event Handler for JournalEntry.
 *
 * Listens to all JournalEntry domain events and updates the read model (JournalEntryView).
 * This is the CQRS (Command Query Responsibility Segregation) pattern:
 * - Commands modify the aggregate (write model) and emit events
 * - Events are handled here to update the read model (denormalized view for fast queries)
 * - Queries read from the view repository (not the aggregate)
 *
 * Flow:
 * 1. Command arrives → Aggregate processes → Events emitted
 * 2. @EventHandler methods triggered → Read model updated
 * 3. Queries use JournalEntryViewReadService → Fast reads from denormalized view
 */
@Component
class JournalEntryViewEventHandler(
    private val repository: JournalEntryViewRepository
) {

    /**
     * Create a new read model view when entry is created.
     */
    @EventHandler
    fun on(event: JournalEntryCreatedEvent) {
        val view = JournalEntryView(
            journalEntryId = event.journalEntryId,
            journalId = event.journalId,
            title = event.title,
            content = event.content,
            status = JournalEntry.EntryStatus.DRAFT,
            createdAt = LocalDateTime.now()
        )
        repository.save(view)
    }

    /**
     * Update read model when title is updated.
     */
    @EventHandler
    fun on(event: EntryTitleUpdatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(title = event.newTitle, editedAt = LocalDateTime.now()))
    }

    /**
     * Update read model when content is updated.
     */
    @EventHandler
    fun on(event: EntryContentUpdatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(content = event.newContent, editedAt = LocalDateTime.now()))
    }

    /**
     * Update read model when entry is validated.
     */
    @EventHandler
    fun on(event: JournalEntryValidatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(
            status = JournalEntry.EntryStatus.VALIDATED,
            reviewedBy = event.validatedBy,
            reviewedAt = LocalDateTime.now()
        ))
    }

    /**
     * Update read model when entry is rejected.
     */
    @EventHandler
    fun on(event: JournalEntryRejectedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(
            status = JournalEntry.EntryStatus.REJECTED,
            reviewedBy = event.rejectedBy,
            reviewReason = event.rejectionReason,
            reviewedAt = LocalDateTime.now()
        ))
    }
}


