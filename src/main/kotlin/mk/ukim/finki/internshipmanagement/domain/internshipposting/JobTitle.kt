package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable


@Embeddable
data class JobTitle(val value: String = "") {

    init {
        val trimmed = value.trim()

        require(trimmed.isNotBlank()) { "Title cannot be blank" }
        require(trimmed.length in 3..200) {
            "Title must be between 3 and 200 characters (got ${trimmed.length})"
        }
    }

    fun getLength(): Int = value.length

    override fun toString(): String = value
}