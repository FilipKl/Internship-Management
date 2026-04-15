package mk.ukim.finki.internshipmanagement.infrastructure.repository

import org.springframework.context.annotation.Configuration

/**
 * Configuration for custom JPA repositories and identifier converters.
 * Provides automatic conversion between typed identifiers and their database representation.
 *
 * Note: Individual ID converters are now defined within their respective ID classes
 * using @Converter(autoApply = true) for better organization and maintainability.
 */
@Configuration
class RepositoryConfiguration {
}

