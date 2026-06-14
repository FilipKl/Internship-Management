package mk.ukim.finki.internshipmanagement.domain.common

import java.time.LocalDateTime
import java.util.*

abstract class DomainEvent(
    val aggregateId: String,
    val eventId: String = UUID.randomUUID().toString(),
    val occurredAt: LocalDateTime = LocalDateTime.now()
) {
    abstract fun getEventType(): String
}

