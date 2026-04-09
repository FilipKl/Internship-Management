package mk.ukim.finki.internshipmanagement.infrastructure.messaging

/**
 * Repository interface for event messaging.
 * This is the port that the adapter (KafkaMessagingRepositoryImpl) implements.
 */
interface EventMessagingRepository {
    fun send(topic: String, key: String, payload: String)
}

