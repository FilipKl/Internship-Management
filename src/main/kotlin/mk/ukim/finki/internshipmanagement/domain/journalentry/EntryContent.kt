package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.Embeddable

/**
 * Value object representing the content/body of a journal entry.
 * 
 * Constraints:
 * - Must be non-blank (trimmed)
 * - Must be at least 10 characters (meaningful reflection, not just a sentence)
 * - Must not exceed 50,000 characters (prevent database abuse)
 */
@Embeddable
data class EntryContent(val value: String = "") {
    
    init {
        val trimmed = value.trim()
        
        require(trimmed.isNotBlank()) { "Entry content cannot be blank" }
        require(trimmed.length >= 10) {
            "Entry content must be at least 10 characters for a meaningful reflection (got ${trimmed.length})"
        }
        require(trimmed.length <= 50000) {
            "Entry content cannot exceed 50,000 characters (got ${trimmed.length})"
        }
    }
    
    fun getLength(): Int = value.length
    
    /**
     * Get a preview of the content (first 100 characters).
     */
    fun getPreview(): String = if (value.length > 100) "${value.substring(0, 100)}..." else value
    
    override fun toString(): String = value
}

