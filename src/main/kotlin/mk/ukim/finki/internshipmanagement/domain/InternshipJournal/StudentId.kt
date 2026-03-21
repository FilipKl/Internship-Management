package mk.ukim.finki.internshipmanagement.domain.InternshipJournal


import jakarta.persistence.Embeddable
import java.util.UUID


@Embeddable
data class StudentId(val value: UUID) {
    init {
        require(value != UUID(0, 0)) { "StudentId cannot be an empty UUID" }
    }


    override fun toString(): String = value.toString()
}