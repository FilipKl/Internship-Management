package mk.ukim.finki.internshipmanagement.domain.internshiprequest.events

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateDateOfDecisionCommand


data class DateOfDecisionUpdatedEvent(
    override val internshipRequestId: InternshipRequestId,
    val dateOfDecision: DecisionDate
) : InternshipRequestEvent(internshipRequestId) {
    constructor(command: UpdateDateOfDecisionCommand) : this(
        internshipRequestId = command.internshipRequestId,
        dateOfDecision = command.dateOfDecision
    )
}