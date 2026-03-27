package mk.ukim.finki.internshipmanagement.infrastructure.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCreatedEvent
import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

/**
 * KafkaEventPublisher acts as a bridge between Axon's internal event bus
 * and the external Kafka message broker.
 *
 * When an InternshipJournalCreatedEvent is published within Axon,
 * this publisher automatically serializes it to JSON and sends it to Kafka.
 *
 * Publishing to Kafka ensures that other microservices (like StudentRecord)
 * can subscribe to these events without tight coupling.
 */
@Service
class KafkaEventPublisher(private val kafkaTemplate: KafkaTemplate<String, String>) {

    private val logger = LoggerFactory.getLogger(KafkaEventPublisher::class.java)

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


    @EventHandler
    fun on(event: InternshipJournalCreatedEvent) {
        try {
            val eventJSON = objectMapper.writeValueAsString(event)
            kafkaTemplate.send(
                "internship-journal-events",
                event.id.getValue(),
                eventJSON
            ).whenComplete { result, ex ->
                if (ex != null) {
                    logger.error(
                        "Failed to publish InternshipJournalCreatedEvent [{}] to Kafka: {}",
                        event.id.getValue(),
                        ex.message,
                        ex
                    )
                } else {
                    logger.info(
                        "Published InternshipJournalCreatedEvent [{}] to topic {} partition {} offset {}",
                        event.id.getValue(),
                        result.recordMetadata.topic(),
                        result.recordMetadata.partition(),
                        result.recordMetadata.offset()
                    )
                }
            }
        } catch (ex: Exception) {
            logger.error(
                "Failed to serialize InternshipJournalCreatedEvent: {}",
                ex.message,
                ex
            )
        }
    }
}


