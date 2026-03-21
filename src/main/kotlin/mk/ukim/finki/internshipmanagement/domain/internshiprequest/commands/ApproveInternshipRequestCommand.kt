package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ApproveInternshipRequestCommand(
    @TargetAggregateIdentifier
    val internshipRequestId: InternshipRequestId
)