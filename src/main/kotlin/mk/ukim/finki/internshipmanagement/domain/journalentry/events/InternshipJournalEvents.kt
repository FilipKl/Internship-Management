package mk.ukim.finki.internshipmanagement.domain.journalentry.events

import mk.ukim.finki.internshipmanagement.domain.common.DomainEvent
import java.time.LocalDateTime
import java.util.*


data class JournalOpenedEvent(
    val journalId: String,
    val studentId: String,
    val openedAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(journalId, UUID.randomUUID().toString(), openedAt) {
    override fun getEventType() = "JournalOpened"
}


data class JournalClosedEvent(
    val journalId: String,
    val closedAt: LocalDateTime = LocalDateTime.now()
) : DomainEvent(journalId, UUID.randomUUID().toString(), closedAt) {
    override fun getEventType() = "JournalClosed"
}

