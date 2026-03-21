package mk.ukim.finki.internshipmanagement.domain.journalentry.events

import mk.ukim.finki.internshipmanagement.domain.common.DomainEvent
import java.time.LocalDateTime
import java.util.*

/**
 * Event published by InternshipJournal aggregate.
 * Used for cross-aggregate reactions.
 * JournalEntry listens to this event to ensure entries are not added to closed journals.
 */
data class JournalOpenedEvent(
    val journalId: String,
    val studentId: String,
    val openedAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(journalId, UUID.randomUUID().toString(), openedAt) {
    override fun getEventType() = "JournalOpened"
}

/**
 * Event published by InternshipJournal aggregate when a journal is closed.
 * JournalEntry listens to this and prevents new entries from being added.
 */
data class JournalClosedEvent(
    val journalId: String,
    val closedAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(journalId, UUID.randomUUID().toString(), closedAt) {
    override fun getEventType() = "JournalClosed"
}

