package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable

@Embeddable
data class Description(val value: String = "") {

    init {
        val trimmed = value.trim()

        require(trimmed.isNotBlank()) { "Description cannot be blank" }
        require(trimmed.length >= 10) { "Description must be at least 10 characters" }
        require(trimmed.length <= 5000) { "Description cannot exceed 5000 characters" }
    }

    fun getLength(): Int = value.length

    fun getPreview(): String = if (value.length > 100) "${value.substring(0, 100)}..." else value

    override fun toString(): String = value
}