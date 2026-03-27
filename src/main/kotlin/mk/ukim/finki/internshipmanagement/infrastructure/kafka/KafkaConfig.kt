package mk.ukim.finki.internshipmanagement.infrastructure.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties

/**
 * KafkaConfig provides Spring Kafka configuration for both producer and consumer.
 *
 * Producer Configuration:
 * - Serializes messages to JSON
 * - Uses "acks=all" for maximum durability (waits for all replicas to acknowledge)
 *
 * Consumer Configuration:
 * - Deserializes messages as Strings
 * - Starts from the earliest offset if no offset exists (auto-offset-reset=earliest)
 * - Uses the group-id from application.properties
 */
@EnableKafka
@Configuration
class KafkaConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    @Value("\${spring.kafka.consumer.group-id}")
    private lateinit var groupId: String

    /**
     * Producer Factory: configures how to send messages to Kafka
     */
    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configs = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to "all", // Wait for all in-sync replicas to acknowledge
            ProducerConfig.RETRIES_CONFIG to 3, // Retry up to 3 times on failure
            ProducerConfig.RETRY_BACKOFF_MS_CONFIG to 100, // 100ms backoff between retries
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true // Exactly-once semantics
        )
        return DefaultKafkaProducerFactory(configs)
    }

    /**
     * Kafka Template: high-level abstraction for sending messages
     */
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    /**
     * Consumer Factory: configures how to receive messages from Kafka
     */
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configs = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest", // Start from beginning if no offset
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true, // Auto-commit offsets
            ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to 1000, // Commit every 1 second
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 100 // Max 100 records per poll
        )
        return DefaultKafkaConsumerFactory(configs)
    }

    /**
     * Kafka Listener Container Factory: manages the listener containers
     */
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConsumerFactory(consumerFactory())
        factory.setConcurrency(3) // Use 3 consumer threads for parallel processing
        factory.containerProperties.ackMode = ContainerProperties.AckMode.BATCH
        return factory
    }
}



