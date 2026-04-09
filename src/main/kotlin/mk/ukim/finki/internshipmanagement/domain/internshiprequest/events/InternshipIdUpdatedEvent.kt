package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateInternshipIdCommand

/**
 * Internal event: Internship ID updated
 * Does NOT publish to Kafka
 */
data class InternshipIdUpdatedEvent(
    override val internshipRequestId: InternshipRequestId,
    val internshipId: InternshipId
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: UpdateInternshipIdCommand) : this(
        internshipRequestId = command.internshipRequestId,
        internshipId = command.internshipId
    )
}