package mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

/**
 * Anti-Corruption Layer: External DTOs
 *
 * These DTOs represent the shape of events coming FROM other bounded contexts
 * (like PartnersManagement). They are NOT domain objects, but rather defensive
 * representations of someone else's model.
 *
 * Using @JsonIgnoreProperties(ignoreUnknown = true) makes these DTOs resilient
 * to schema evolution: if the external service adds new fields, this service won't break.
 *
 * Nullable fields indicate fields that this service doesn't care about.
 */

/**
 * PartnerRegisteredEventDTO represents an event from PartnersManagement service.
 * When a partner is registered, this event is published to Kafka.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PartnerRegisteredEventDTO(
    val partnerId: String?,           // Nullable - we don't use it for internships
    val partnerName: String,           // Required - we need to know the partner/company name
    val partnerEmail: String?,         // Nullable - nice to have but not critical
    val registrationDate: LocalDateTime? // Nullable - not critical for internship tracking
)

/**
 * PartnerUpdatedEventDTO represents an event when partner info is updated.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PartnerUpdatedEventDTO(
    val partnerId: String?,
    val partnerName: String?,
    val partnerEmail: String?,
    val updatedAt: LocalDateTime?
)

/**
 * PartnerDeactivatedEventDTO represents when a partner is deactivated.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class PartnerDeactivatedEventDTO(
    val partnerId: String?,
    val partnerName: String?,
    val deactivationReason: String?
)

