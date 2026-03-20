package mk.ukim.finki.internshipmanagement.domain.common

import jakarta.persistence.Transient

/**
 * Base class for aggregate roots in domain-driven design.
 * Aggregates are responsible for maintaining their own state and recording domain events.
 */
abstract class AggregateRoot {
    
    @Transient
    private val uncommittedEvents: MutableList<DomainEvent> = mutableListOf()
    
    /**
     * Record a domain event.
     * Events represent what has happened in the domain.
     */
    protected fun recordEvent(event: DomainEvent) {
        uncommittedEvents.add(event)
    }
    
    /**
     * Get all uncommitted events and return as immutable list.
     */
    fun getUncommittedEvents(): List<DomainEvent> = uncommittedEvents.toList()
    
    /**
     * Clear the uncommitted events list.
     * Called after events are persisted.
     */
    fun clearUncommittedEvents() {
        uncommittedEvents.clear()
    }
}

