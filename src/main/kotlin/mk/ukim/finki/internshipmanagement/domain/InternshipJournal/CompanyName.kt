package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import jakarta.persistence.Embeddable

@Embeddable
data class CompanyName(val value: String = "") {

    override fun toString(): String = value
}

