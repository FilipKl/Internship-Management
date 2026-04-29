package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import jakarta.persistence.Embeddable

@Embeddable
data class ProfessorId(val value: String = "") {
    override fun toString(): String = value
}