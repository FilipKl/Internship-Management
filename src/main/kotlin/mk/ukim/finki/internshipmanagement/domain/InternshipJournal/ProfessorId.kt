package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
data class ProfessorId(val value: UUID) {
    init {
        // Domain rule: UUID must not be all zeros (trivial but valid)
        require(value != UUID(0, 0)) { "ProfessorId cannot be an empty UUID" }
    }


    override fun toString(): String = value.toString()
}