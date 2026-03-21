package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.ApproveInternshipRequestCommand

data class InternshipRequestApprovedEvent(
    val internshipRequestId: InternshipRequestId
) {
    constructor(command: ApproveInternshipRequestCommand) : this(
        internshipRequestId = command.internshipRequestId
    )
}