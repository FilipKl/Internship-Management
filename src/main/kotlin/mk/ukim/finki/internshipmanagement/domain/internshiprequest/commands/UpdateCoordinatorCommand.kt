package mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class UpdateCoordinatorCommand(
    @TargetAggregateIdentifier
    val internshipRequestId: InternshipRequestId,
    val coordinatorId: CoordinatorId
)