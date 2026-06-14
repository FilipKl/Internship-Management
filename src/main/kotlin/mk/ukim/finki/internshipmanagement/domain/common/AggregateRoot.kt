package mk.ukim.finki.internshipmanagement.domain.common

import jakarta.persistence.Transient


abstract class AggregateRoot {
    
    @Transient
    private val uncommittedEvents: MutableList<Any> = mutableListOf()


    protected fun recordEvent(event: Any) {
        uncommittedEvents.add(event)
    }

    fun getUncommittedEvents(): List<Any> = uncommittedEvents.toList()

    fun clearUncommittedEvents() {
        uncommittedEvents.clear()
    }
}

