package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import java.time.LocalDate

/**
 * External event: InternshipRequest submitted
 * Published to Kafka for other microservices
 */
data class InternshipRequestSubmittedExternalEvent(
    val id: InternshipRequestId,
    val studentId: StudentId,
    val companyId: CompanyId,
    val internshipId: InternshipId,
    val dateOfCreation: LocalDate
)

data class InternshipRequestSubmittedEvent(
    override val internshipRequestId: InternshipRequestId,
    val studentId: StudentId,
    val companyId: CompanyId,
    val internshipId: InternshipId,
    val coordinatorId: CoordinatorId,
    val dateOfCreation: LocalDate
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: SubmitInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId,
        studentId = command.studentId,
        companyId = command.companyId,
        internshipId = command.internshipId,
        coordinatorId = command.coordinatorId,
        dateOfCreation = command.dateOfCreation
    )

    override fun toExternalEvent(): InternshipRequestSubmittedExternalEvent {
        return InternshipRequestSubmittedExternalEvent(
            id = internshipRequestId,
            studentId = studentId,
            companyId = companyId,
            internshipId = internshipId,
            dateOfCreation = dateOfCreation
        )
    }
}
