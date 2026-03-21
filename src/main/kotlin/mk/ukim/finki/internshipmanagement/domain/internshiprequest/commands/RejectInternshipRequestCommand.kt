package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RejectInternshipRequestCommand(
    @TargetAggregateIdentifier
    val internshipRequestId: InternshipRequestId
)