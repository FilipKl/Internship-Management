package mk.ukim.finki.internshipmanagement.infrastructure.messaging.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mk.ukim.finki.internshipmanagement.domain.common.AbstractEvent
import mk.ukim.finki.internshipmanagement.infrastructure.messaging.EventMessagingService
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

/**
 * Generic event handler that publishes ANY AbstractEvent to Kafka.
 *
 * This replaces the hardcoded KafkaEventPublisher that only listened to one event type.
 *
 * Flow:
 * 1. Any aggregate emits an event extending AbstractEvent
 * 2. Axon's event bus triggers this handler
 * 3. Handler calls event.toExternalEvent()
 * 4. If null is returned, skip publishing (event is internal)
 * 5. If object is returned, serialize it and send to Kafka
 * 6. Topic is auto-derived from class name (event.eventTopic())
 * 7. Key is the aggregate identifier (event.identifier.value)
 */
@Component
class EventMessagingEventHandler(
    val eventMessagingService: EventMessagingService
) {

    private val objectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    /**
     * Listen to ANY event that extends AbstractEvent.
     * @EventHandler makes Axon invoke this method when ANY AbstractEvent is emitted.
     */
    @EventHandler
    fun on(event: AbstractEvent) {
        // Step 1: Ask the event to convert itself to external form
        val externalEvent = event.toExternalEvent() ?: return
        // If null, the event is internal - don't publish

        // Step 2: Serialize the external event to JSON
        val eventJSON = objectMapper.writeValueAsString(externalEvent)

        // Step 3: Send to Kafka
        eventMessagingService.send(
            topic = event.eventTopic(),  // Auto-derived: "internship.journal.created"
            key = event.identifier.getValue().toString(),  // Aggregate ID for partitioning
            payload = eventJSON
        )
    }
}


