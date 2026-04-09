package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateCoordinatorCommand

/**
 * Internal event: Coordinator updated
 * Does NOT publish to Kafka
 */
data class CoordinatorUpdatedEvent(
    override val internshipRequestId: InternshipRequestId,
    val coordinatorId: CoordinatorId
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: UpdateCoordinatorCommand) : this(
        internshipRequestId = command.internshipRequestId,
        coordinatorId = command.coordinatorId
    )
}