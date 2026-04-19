package mk.ukim.finki.internshipmanagement.presentation.kafka.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ExecutionException
import org.apache.kafka.clients.producer.RecordMetadata

/**
 * REST Controller for Kafka Management operations.
 * Provides HTTP endpoints for monitoring Kafka topics, publishing messages, and managing Kafka infrastructure.
 *
 * This controller enables:
 * - Publishing messages to Kafka topics
 * - Monitoring topic information
 * - Health checks for Kafka connectivity
 * - Message publishing for testing purposes
 */
@RestController
@RequestMapping("/api/v1/kafka")
class KafkaManagementController(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    private val logger = LoggerFactory.getLogger(KafkaManagementController::class.java)

    @Value("\${spring.kafka.bootstrap-servers:localhost:9092}")
    private lateinit var bootstrapServers: String

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())

    /**
     * Health check endpoint for Kafka connectivity.
     * Verifies that the application can connect to the Kafka broker.
     *
     * @return ResponseEntity with status "UP" if Kafka is reachable, "DOWN" otherwise
     */
    @GetMapping("/health")
    fun kafkaHealth(): ResponseEntity<Map<String, Any>> {
        return try {
            val config = mapOf(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG to 5000
            )
            AdminClient.create(config).use { adminClient ->
                val clusterId = adminClient.describeCluster().clusterId().get()
                logger.info("Kafka health check successful - Cluster ID: {}", clusterId)
                ResponseEntity.ok(mapOf(
                    "status" to "UP",
                    "message" to "Kafka broker is reachable",
                    "clusterId" to clusterId,
                    "bootstrapServers" to bootstrapServers
                ))
            }
        } catch (ex: Exception) {
            logger.error("Kafka health check failed", ex)
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(mapOf(
                "status" to "DOWN",
                "message" to "Kafka broker is unreachable",
                "error" to (ex.message ?: "Unknown error"),
                "bootstrapServers" to bootstrapServers
            ))
        }
    }

    /**
     * Publish a message to a specified Kafka topic.
     *
     * @param topicName The name of the topic to publish to
     * @param request The request containing the message key and value
     * @return ResponseEntity with the partition and offset of the published message
     */
    @PostMapping("/publish/{topicName}")
    fun publishMessage(
        @PathVariable topicName: String,
        @RequestBody request: PublishMessageRequest
    ): ResponseEntity<PublishMessageResponse> {
        return try {
            if (topicName.isBlank()) {
                return ResponseEntity.badRequest().body(
                    PublishMessageResponse(
                        success = false,
                        message = "Topic name cannot be blank",
                        topic = topicName,
                        messagePartition = -1,
                        messageOffset = -1L
                    )
                )
            }

            if (request.message.isBlank()) {
                return ResponseEntity.badRequest().body(
                    PublishMessageResponse(
                        success = false,
                        message = "Message value cannot be blank",
                        topic = topicName,
                        messagePartition = -1,
                        messageOffset = -1L
                    )
                )
            }

            val producerRecord = ProducerRecord(
                topicName,
                request.key,
                request.message
            )

            val metadata = kafkaTemplate.send(producerRecord).get() as RecordMetadata
            val partition = metadata.partition()
            val offset = metadata.offset()

            logger.info(
                "Message published to topic [{}] - Partition: {}, Offset: {}",
                topicName,
                partition,
                offset
            )

            ResponseEntity.ok(
                PublishMessageResponse(
                    success = true,
                    message = "Message published successfully",
                    topic = topicName,
                    messagePartition = partition,
                    messageOffset = offset
                )
            )
        } catch (ex: Exception) {
            logger.error("Failed to publish message to topic [{}]", topicName, ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                PublishMessageResponse(
                    success = false,
                    message = "Failed to publish message: ${ex.message}",
                    topic = topicName,
                    messagePartition = -1,
                    messageOffset = -1L
                )
            )
        }
    }

    /**
     * Publish a JSON object as a message to a Kafka topic.
     * The object is automatically serialized to JSON format.
     *
     * @param topicName The name of the topic to publish to
     * @param request The request containing the message key and JSON payload
     * @return ResponseEntity with the partition and offset of the published message
     */
    @PostMapping("/publish-json/{topicName}")
    fun publishJsonMessage(
        @PathVariable topicName: String,
        @RequestBody request: PublishJsonMessageRequest
    ): ResponseEntity<PublishMessageResponse> {
        return try {
            if (topicName.isBlank()) {
                return ResponseEntity.badRequest().body(
                    PublishMessageResponse(
                        success = false,
                        message = "Topic name cannot be blank",
                        topic = topicName,
                        messagePartition = -1,
                        messageOffset = -1L
                    )
                )
            }

            val jsonString = objectMapper.writeValueAsString(request.payload)

            val producerRecord = ProducerRecord(
                topicName,
                request.key,
                jsonString
            )

            val metadata = kafkaTemplate.send(producerRecord).get() as RecordMetadata
            val partition = metadata.partition()
            val offset = metadata.offset()

            logger.info(
                "JSON message published to topic [{}] - Partition: {}, Offset: {}",
                topicName,
                partition,
                offset
            )

            ResponseEntity.ok(
                PublishMessageResponse(
                    success = true,
                    message = "JSON message published successfully",
                    topic = topicName,
                    messagePartition = partition,
                    messageOffset = offset
                )
            )
        } catch (ex: Exception) {
            logger.error("Failed to publish JSON message to topic [{}]", topicName, ex)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                PublishMessageResponse(
                    success = false,
                    message = "Failed to publish JSON message: ${ex.message}",
                    topic = topicName,
                    messagePartition = -1,
                    messageOffset = -1L
                )
            )
        }
    }

    /**
     * Get information about Kafka broker connectivity.
     *
     * @return ResponseEntity with broker information
     */
    @GetMapping("/info")
    fun getKafkaInfo(): ResponseEntity<Map<String, Any>> {
        return try {
            val config = mapOf(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG to 5000
            )
            AdminClient.create(config).use { adminClient ->
                val nodes = adminClient.describeCluster().nodes().get()
                val clusterId = adminClient.describeCluster().clusterId().get()

                val nodeInfo = nodes.map { node ->
                    mapOf(
                        "id" to node.id(),
                        "host" to node.host(),
                        "port" to node.port(),
                        "rack" to (node.rack() ?: "unknown")
                    )
                }

                ResponseEntity.ok(mapOf(
                    "bootstrapServers" to bootstrapServers,
                    "clusterId" to clusterId,
                    "nodeCount" to nodes.size,
                    "nodes" to nodeInfo
                ))
            }
        } catch (ex: Exception) {
            logger.error("Failed to retrieve Kafka info", ex)
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(mapOf(
                "error" to "Failed to retrieve Kafka information",
                "message" to (ex.message ?: "Unknown error"),
                "bootstrapServers" to bootstrapServers
            ))
        }
    }

    /**
     * Request class for publishing plain text messages to Kafka.
     */
    data class PublishMessageRequest(
        val key: String? = null,
        val message: String = ""
    )

    /**
     * Request class for publishing JSON messages to Kafka.
     */
    data class PublishJsonMessageRequest(
        val key: String? = null,
        val payload: Any = mapOf<String, Any>()
    )

    /**
     * Response class for message publishing operations.
     */
    data class PublishMessageResponse(
        val success: Boolean,
        val message: String,
        val topic: String,
        val messagePartition: Int,
        val messageOffset: Long
    ) {
        // For backward compatibility, provide partition and offset as well
        @Suppress("unused")
        val partition: Int get() = messagePartition

        @Suppress("unused")
        val offset: Long get() = messageOffset
    }
}











