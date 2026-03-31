package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import java.time.LocalDate

data class InternshipRequestSubmittedEvent(
    val internshipRequestId: InternshipRequestId,
    val studentId: StudentId,
    val companyId: CompanyId,
    val internshipId: InternshipId,
    val coordinatorId: CoordinatorId,
    val dateOfCreation: LocalDate
) {
    constructor(command: SubmitInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId,
        studentId = command.studentId,
        companyId = command.companyId,
        internshipId = command.internshipId,
        coordinatorId = command.coordinatorId,
        dateOfCreation = command.dateOfCreation
    )
}
