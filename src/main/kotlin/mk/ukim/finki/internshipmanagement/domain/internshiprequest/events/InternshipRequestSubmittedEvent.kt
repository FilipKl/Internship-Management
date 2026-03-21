package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import java.time.LocalDate

data class InternshipRequestSubmittedEvent(
    val internshipRequestId: InternshipRequestId,
    val studentId: String,
    val companyId: String,
    val internshipId: String,
    val coordinatorId: String,
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
