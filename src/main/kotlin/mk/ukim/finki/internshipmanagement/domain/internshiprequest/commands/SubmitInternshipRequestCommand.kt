package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import java.time.LocalDate

data class SubmitInternshipRequestCommand(
    val internshipRequestId: InternshipRequestId = InternshipRequestId.generate(),
    val studentId: StudentId,
    val companyId: CompanyId,
    val internshipId: InternshipId,
    val coordinatorId: CoordinatorId,
    val dateOfCreation: LocalDate = LocalDate.now()
)




