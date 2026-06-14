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


@Service
class KafkaEventConsumer(
    private val partnerEventTranslator: PartnerEventTranslator
) {

    private val logger = LoggerFactory.getLogger(KafkaEventConsumer::class.java)

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())


    @KafkaListener(
        topics = ["partner-events"],
        groupId = "internship-management-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listenToPartnerEvents(record: ConsumerRecord<String, String>) {
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
                        "Processed external event: Partner registered [{}]",
                        partnerRegistered.partnerName
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
                        "Processed external event: Partner updated [{}]",
                        partnerUpdated.partnerName
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
                        "Processed external event: Partner deactivated [{}] (Reason: {})",
                        partnerDeactivated.partnerName,
                        partnerDeactivated.deactivationReason
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

