package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class UpdateDateOfDecisionCommand(
    @TargetAggregateIdentifier
    val internshipRequestId: InternshipRequestId,
    val dateOfDecision: DecisionDate
)