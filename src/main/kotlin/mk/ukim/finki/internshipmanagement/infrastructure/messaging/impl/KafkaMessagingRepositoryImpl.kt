package mk.ukim.finki.internshipmanagement.infrastructure.messaging.impl

import mk.ukim.finki.internshipmanagement.infrastructure.messaging.EventMessagingRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Repository

/**
 * Kafka adapter: ONLY class that imports KafkaTemplate.
 *
 * This is the only place in the application where Kafka-specific code should appear.
 * All other layers use the EventMessagingService/Repository interfaces.
 *
 * This ensures that if we want to switch from Kafka to another message broker,
 * we only need to modify this one class.
 */
@Repository
class KafkaMessagingRepositoryImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>
) : EventMessagingRepository {

    private val logger = LoggerFactory.getLogger(KafkaMessagingRepositoryImpl::class.java)

    override fun send(topic: String, key: String, payload: String) {
        kafkaTemplate.send(topic, key, payload)
            .whenComplete { result, ex ->
                if (ex != null) {
                    logger.error(
                        "Failed to publish event [{}] to Kafka: {}",
                        key, ex.message, ex
                    )
                } else {
                    logger.info(
                        "Published event [{}] to topic {} partition {} offset {}",
                        key,
                        result.recordMetadata.topic(),
                        result.recordMetadata.partition(),
                        result.recordMetadata.offset()
                    )
                }
            }
    }
}

