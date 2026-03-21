package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import java.time.LocalDate
import java.util.UUID

data class SubmitInternshipRequestCommand(
    val internshipRequestId: InternshipRequestId = InternshipRequestId(UUID.randomUUID()),
    val studentId: String,
    val companyId: String,
    val internshipId: String,
    val coordinatorId: String,
    val dateOfCreation: LocalDate = LocalDate.now()
)




