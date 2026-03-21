package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import jakarta.persistence.Embeddable

@Embeddable
data class CompanyName(val value: String = "") {
    init {
        require(value.isNotBlank()) { "Company name cannot be blank" }
        require(value.length <= 100) { "Company name cannot exceed 100 characters" }
    }

    override fun toString(): String = value
}

