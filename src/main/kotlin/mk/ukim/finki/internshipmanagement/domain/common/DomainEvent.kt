package mk.ukim.finki.internshipmanagement.domain.common

import java.time.LocalDateTime
import java.util.*

/**
 * Base class for all domain events.
 * Domain events represent something that has happened in the domain.
 */
abstract class DomainEvent(
    val aggregateId: String,
    val eventId: String = UUID.randomUUID().toString(),
    val occurredAt: LocalDateTime = LocalDateTime.now()
) {
    abstract fun getEventType(): String
}

