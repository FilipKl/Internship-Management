package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.Embeddable


@Embeddable
data class EntryContent(val value: String = "") {
    
    init {
        if (value.isNotEmpty()) {
            val trimmed = value.trim()

            require(trimmed.isNotBlank()) { "Entry content cannot be blank" }
            require(trimmed.length >= 10) {
                "Entry content must be at least 10 characters for a meaningful reflection (got ${trimmed.length})"
            }
            require(trimmed.length <= 50000) {
                "Entry content cannot exceed 50,000 characters (got ${trimmed.length})"
            }
        }
    }
    
    fun getLength(): Int = value.length

    fun getPreview(): String = if (value.length > 100) "${value.substring(0, 100)}..." else value
    
    override fun toString(): String = value
}

