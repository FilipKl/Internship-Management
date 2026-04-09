package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.RejectInternshipRequestCommand

/**
 * External event: InternshipRequest rejected
 * Published to Kafka for other microservices
 */
data class InternshipRequestRejectedExternalEvent(
    val id: InternshipRequestId
)

data class InternshipRequestRejectedEvent(
    override val internshipRequestId: InternshipRequestId
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: RejectInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId
    )

    override fun toExternalEvent(): InternshipRequestRejectedExternalEvent {
        return InternshipRequestRejectedExternalEvent(
            id = internshipRequestId
        )
    }
}