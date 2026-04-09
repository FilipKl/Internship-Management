package mk.ukim.finki.internshipmanagement.domain.common

import jakarta.persistence.Transient

/**
 * Base class for aggregate roots in domain-driven design.
 * Aggregates are responsible for maintaining their own state and recording domain events.
 *
 * Events are recorded as Any type to support both legacy DomainEvent and new AbstractEvent classes.
 */
abstract class AggregateRoot {
    
    @Transient
    private val uncommittedEvents: MutableList<Any> = mutableListOf()

    /**
     * Record a domain event (AbstractEvent or DomainEvent).
     * Events represent what has happened in the domain.
     */
    protected fun recordEvent(event: Any) {
        uncommittedEvents.add(event)
    }
    
    /**
     * Get all uncommitted events and return as immutable list.
     */
    fun getUncommittedEvents(): List<Any> = uncommittedEvents.toList()

    /**
     * Clear the uncommitted events list.
     * Called after events are persisted.
     */
    fun clearUncommittedEvents() {
        uncommittedEvents.clear()
    }
}

