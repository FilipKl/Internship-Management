package mk.ukim.finki.internshipmanagement.infrastructure.messaging

/**
 * Service interface for publishing events to messaging infrastructure.
 * This interface hides the implementation details (Kafka, RabbitMQ, etc.)
 * from the domain layer.
 */
interface EventMessagingService {
    fun send(topic: String, key: String, payload: String)
}

