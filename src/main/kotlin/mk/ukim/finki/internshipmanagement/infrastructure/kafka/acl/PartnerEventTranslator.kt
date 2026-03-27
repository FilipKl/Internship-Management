package mk.ukim.finki.internshipmanagement.infrastructure.kafka.acl

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.CompanyName
import org.springframework.stereotype.Service

/**
 * PartnerEventTranslator is the Anti-Corruption Layer translator.
 * It converts external DTOs (from PartnersManagement) to internal domain commands.
 *
 * Why is this necessary?
 * - The PartnersManagement service speaks in terms of "partners" with fields like
 *   partnerId, partnerEmail, registrationDate.
 * - The InternshipManagement service only cares about the company/partner name.
 * - This translator extracts exactly what's needed and discards the rest.
 *
 * Benefits:
 * - If PartnersManagement changes their event schema, only this translator needs updating
 * - The domain model remains clean and independent
 * - We explicitly control what information crosses the bounded context boundary
 */
@Service
class PartnerEventTranslator {

    /**
     * Translates an incoming partner registration event to a CompanyName value object.
     * This is what we actually need in the InternshipManagement context.
     */
    fun toCompanyName(event: PartnerRegisteredEventDTO): CompanyName {
        return CompanyName(
            value = event.partnerName
        )
    }

    /**
     * Translates an updated partner event to a CompanyName.
     * Returns null if the partner name is not provided (resilient to schema changes).
     */
    fun toCompanyNameUpdate(event: PartnerUpdatedEventDTO): CompanyName? {
        return event.partnerName?.let { CompanyName(value = it) }
    }

    /**
     * For deactivation events, we just need the company name to identify which
     * internships might be affected.
     */
    fun toCompanyNameFromDeactivation(event: PartnerDeactivatedEventDTO): CompanyName? {
        return event.partnerName?.let { CompanyName(value = it) }
    }
}

