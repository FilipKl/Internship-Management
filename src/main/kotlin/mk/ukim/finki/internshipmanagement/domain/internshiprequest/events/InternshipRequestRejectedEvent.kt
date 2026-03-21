package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.RejectInternshipRequestCommand

data class InternshipRequestRejectedEvent(
    val internshipRequestId: InternshipRequestId
) {
    constructor(command: RejectInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId
    )
}