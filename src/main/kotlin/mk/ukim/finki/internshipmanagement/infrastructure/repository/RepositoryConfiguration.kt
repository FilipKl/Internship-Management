package mk.ukim.finki.internshipmanagement.infrastructure.repository

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import org.springframework.context.annotation.Configuration

/**
 * Configuration for custom JPA repositories and identifier converters.
 * Provides automatic conversion between typed identifiers and their database representation.
 */
@Configuration
class RepositoryConfiguration {

    /**
     * Custom JPA converter for InternshipPostingId.
     * Enables automatic conversion between InternshipPostingId value objects and String database columns.
     * This is essential for maintaining type safety in the domain model while ensuring proper
     * persistence in the database.
     *
     * Usage: Applied automatically to any @EmbeddedId or @Column field of type InternshipPostingId
     *
     * Example:
     *   @EmbeddedId
     *   lateinit var internshipPostingId: InternshipPostingId
     *   // Converter automatically handles String ↔ InternshipPostingId conversion
     */
    @Converter(autoApply = true)
    class InternshipPostingIdConverter : AttributeConverter<InternshipPostingId, String> {

        /**
         * Convert InternshipPostingId (domain object) to String (database representation).
         *
         * @param attribute The InternshipPostingId value object to convert
         * @return The String representation of the ID (includes prefix), or null if attribute is null
         */
        override fun convertToDatabaseColumn(attribute: InternshipPostingId?): String? {
            return attribute?.getValue()
        }

        /**
         * Convert String (database representation) back to InternshipPostingId (domain object).
         *
         * @param dbData The String value from the database
         * @return The reconstructed InternshipPostingId value object, or null if dbData is null
         */
        override fun convertToEntityAttribute(dbData: String?): InternshipPostingId? {
            return if (dbData != null) InternshipPostingId.from(dbData) else null
        }
    }
}

