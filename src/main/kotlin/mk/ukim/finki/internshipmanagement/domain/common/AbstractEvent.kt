package mk.ukim.finki.internshipmanagement.domain.common

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * AbstractEvent is the Kafka-aware base class for all domain events.
 *
 * It provides:
 * 1. Automatic JSON serialization with _eventType field
 * 2. Automatic Kafka topic derivation from class name
 * 3. Optional external event conversion (for public contracts)
 *
 * All events that should be publishable to Kafka must extend this class
 * (via intermediate aggregate event classes).
 */
abstract class AbstractEvent(
    open val identifier: Identifier<out Any>
) {

    /**
     * Serialize event type as "_eventType" in JSON for debugging and routing.
     * Example: InternshipJournalCreatedEvent → "_eventType": "InternshipJournalCreatedEvent"
     */
    @JsonProperty("_eventType")
    fun eventType(): String = this.javaClass.simpleName

    /**
     * Auto-derive Kafka topic name from event class name.
     * Removes "Event" suffix and converts CamelCase to dot-separated lowercase.
     *
     * Examples:
     *   InternshipJournalCreatedEvent → "internship.journal.created"
     *   JournalEntryAddedEvent → "journal.entry.added"
     *   InternshipPostingPublishedEvent → "internship.posting.published"
     */
    @JsonIgnore
    fun eventTopic(): String =
        this.javaClass.simpleName
            .removeSuffix("Event")  // InternshipJournalCreated
            .replace(Regex("([a-z])([A-Z])"), "$1.$2")  // Internship.Journal.Created
            .lowercase()  // internship.journal.created

    /**
     * Convert internal event to external event for publishing.
     *
     * Default behavior (returns null):
     *   Event stays internal, is NOT published to Kafka
     *
     * Override behavior (returns external event):
     *   Event is published to Kafka with clean external contract
     *
     * Used by EventMessagingEventHandler to decide:
     *   - toExternalEvent() returns null? → Skip publishing
     *   - toExternalEvent() returns object? → Publish the returned object
     */
    @JsonIgnore
    open fun toExternalEvent(): Any? = null
}

