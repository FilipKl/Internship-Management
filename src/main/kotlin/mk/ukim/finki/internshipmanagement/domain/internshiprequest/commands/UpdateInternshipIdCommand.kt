package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class UpdateInternshipIdCommand(
    @TargetAggregateIdentifier
    val internshipRequestId: InternshipRequestId,
    val internshipId: InternshipId
)