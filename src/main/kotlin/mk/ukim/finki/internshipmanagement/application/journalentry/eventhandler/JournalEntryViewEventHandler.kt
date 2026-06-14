package mk.ukim.finki.internshipmanagement.application.journalentry.eventhandler

import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryView
import mk.ukim.finki.internshipmanagement.application.journalentry.query.JournalEntryViewRepository
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.events.*
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class JournalEntryViewEventHandler(
    private val repository: JournalEntryViewRepository
) {
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

    @EventHandler
    fun on(event: EntryTitleUpdatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(title = event.newTitle, editedAt = LocalDateTime.now()))
    }

    @EventHandler
    fun on(event: EntryContentUpdatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(content = event.newContent, editedAt = LocalDateTime.now()))
    }

    @EventHandler
    fun on(event: JournalEntryValidatedEvent) {
        val view = repository.findById(event.journalEntryId).orElseThrow()
        repository.save(view.copy(
            status = JournalEntry.EntryStatus.VALIDATED,
            reviewedBy = event.validatedBy,
            reviewedAt = LocalDateTime.now()
        ))
    }

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


