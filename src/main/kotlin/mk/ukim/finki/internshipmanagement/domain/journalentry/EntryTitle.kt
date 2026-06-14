package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.Embeddable


@Embeddable
data class EntryTitle(val value: String = "") {
    
    init {
        if (value.isNotEmpty()) {
            val trimmed = value.trim()

            require(trimmed.isNotBlank()) { "Entry title cannot be blank" }
            require(trimmed.length in 1..255) {
                "Entry title must be between 1 and 255 characters (got ${trimmed.length})"
            }
        }
    }
    
    fun getLength(): Int = value.length
    
    override fun toString(): String = value
}

