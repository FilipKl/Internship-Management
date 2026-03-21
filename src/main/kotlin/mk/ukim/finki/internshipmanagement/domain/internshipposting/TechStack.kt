package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable

@Embeddable
data class TechStack(val value: String = "") {
    init {
        val trimmed = value.trim()
        require(trimmed.isNotBlank()) { "Tech stack cannot be blank" }
        require(trimmed.length <= 1000) { "Tech stack cannot exceed 1000 characters" }
    }

    override fun toString(): String = value
}