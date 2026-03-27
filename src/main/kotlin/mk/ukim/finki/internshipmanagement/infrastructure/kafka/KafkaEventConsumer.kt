package mk.ukim.finki.internshipmanagement.infrastructure.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl.PartnerDeactivatedEventDTO
import mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl.PartnerEventTranslator
import mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl.PartnerRegisteredEventDTO
import mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl.PartnerUpdatedEventDTO
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

/**
 * KafkaEventConsumer listens to events published by the PartnersManagement service.
 *
 * The consumer group "internship-management-group" ensures that if there are multiple
 * instances of this service, they coordinate consumption and don't duplicate processing.
 *
 * Topics consumed:
 * - "partner-events": events from PartnersManagement (partner registration, updates, deactivation)
 *
 * The flow:
 * 1. Spring Kafka polls the "partner-events" topic
 * 2. For each record, this method deserializes the JSON into an external DTO
 * 3. The PartnerEventTranslator (ACL) converts the external DTO to domain objects
 * 4. We log the event and update our internal state if needed
 *
 * Note: This consumer currently logs events. In a full implementation, you might:
 * - Store partner information in a cache or read model
 * - Validate internship journals against active partners
 * - Trigger notifications or workflows
 */
@Service
class KafkaEventConsumer(
    private val partnerEventTranslator: PartnerEventTranslator
) {

    private val logger = LoggerFactory.getLogger(KafkaEventConsumer::class.java)

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())

    /**
     * Listens to the "partner-events" topic for partner registration events.
     */
    @KafkaListener(
        topics = ["partner-events"],
        groupId = "internship-management-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listenToPartnerRegistration(record: ConsumerRecord<String, String>) {
        try {
            logger.debug("Received partner event: {}", record.value())

            val messageJson = record.value()

            try {
                val partnerRegistered: PartnerRegisteredEventDTO = objectMapper.readValue(
                    messageJson,
                    PartnerRegisteredEventDTO::class.java
                )

                if (partnerRegistered.partnerName != null) {
                    val companyName = partnerEventTranslator.toCompanyName(partnerRegistered)
                    logger.info(
                        "Partner registered: {} - translated to CompanyName: {}",
                        partnerRegistered.partnerName,
                        companyName.value
                    )
                    return
                }
            } catch (ex: Exception) {
                logger.trace("Not a PartnerRegisteredEvent, trying other types")
            }

            try {
                val partnerUpdated: PartnerUpdatedEventDTO = objectMapper.readValue(
                    messageJson,
                    PartnerUpdatedEventDTO::class.java
                )

                if (partnerUpdated.partnerName != null) {
                    val companyName = partnerEventTranslator.toCompanyNameUpdate(partnerUpdated)
                    logger.info(
                        "Partner updated: {} - translated to CompanyName: {}",
                        partnerUpdated.partnerName,
                        companyName?.value
                    )
                    return
                }
            } catch (ex: Exception) {
                logger.trace("Not a PartnerUpdatedEvent, trying other types")
            }

            try {
                val partnerDeactivated: PartnerDeactivatedEventDTO = objectMapper.readValue(
                    messageJson,
                    PartnerDeactivatedEventDTO::class.java
                )

                if (partnerDeactivated.partnerName != null) {
                    val companyName = partnerEventTranslator.toCompanyNameFromDeactivation(partnerDeactivated)
                    logger.info(
                        "Partner deactivated: {} (Reason: {}) - translated to CompanyName: {}",
                        partnerDeactivated.partnerName,
                        partnerDeactivated.deactivationReason,
                        companyName?.value
                    )
                    return
                }
            } catch (ex: Exception) {
                logger.trace("Not a PartnerDeactivatedEvent either")
            }

            logger.warn("Received unrecognized partner event: {}", messageJson)

        } catch (ex: Exception) {
            logger.error("Failed to process partner event from record: {}", record.value(), ex)
        }
    }
}

