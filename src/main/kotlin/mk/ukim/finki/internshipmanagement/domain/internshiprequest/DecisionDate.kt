package mk.ukim.finki.internshipmanagement.domain.internshiprequest
import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
data class DecisionDate(val value: LocalDate = LocalDate.now()) {
    init {
        require(!value.isBefore(LocalDate.now())) {
            "Decision date cannot be in the past"
        }
    }
}