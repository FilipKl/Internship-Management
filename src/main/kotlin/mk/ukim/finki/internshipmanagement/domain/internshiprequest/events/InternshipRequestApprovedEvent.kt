package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.ApproveInternshipRequestCommand

/**
 * External event: InternshipRequest approved
 * Published to Kafka for other microservices
 */
data class InternshipRequestApprovedExternalEvent(
    val id: InternshipRequestId
)

data class InternshipRequestApprovedEvent(
    override val internshipRequestId: InternshipRequestId
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: ApproveInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId
    )

    override fun toExternalEvent(): InternshipRequestApprovedExternalEvent {
        return InternshipRequestApprovedExternalEvent(
            id = internshipRequestId
        )
    }
}