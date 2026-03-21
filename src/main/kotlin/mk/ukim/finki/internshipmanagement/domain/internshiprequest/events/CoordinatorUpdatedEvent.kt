package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateCoordinatorCommand

data class CoordinatorUpdatedEvent(
    val internshipRequestId: InternshipRequestId,
    val coordinatorId: String
) {
    constructor(command: UpdateCoordinatorCommand) : this(
        internshipRequestId = command.internshipRequestId,
        coordinatorId = command.coordinatorId
    )
}