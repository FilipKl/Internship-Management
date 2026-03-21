package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable

/**
 * Value object representing the company name for an internship posting.
 * 
 * Constraints:
 * - Must be non-blank
 * - Must not exceed 255 characters
 */
@Embeddable
data class CompanyName(val value: String = "") {
    init {
        val trimmed = value.trim()
        
        require(trimmed.isNotBlank()) { "Company name cannot be blank" }
        require(trimmed.length <= 255) { "Company name cannot exceed 255 characters" }
    }
    
    override fun toString(): String = value
}

