package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateInternshipIdCommand

data class InternshipIdUpdatedEvent(
    val internshipRequestId: InternshipRequestId,
    val internshipId: InternshipId
) {
    constructor(command: UpdateInternshipIdCommand) : this(
        internshipRequestId = command.internshipRequestId,
        internshipId = command.internshipId
    )
}