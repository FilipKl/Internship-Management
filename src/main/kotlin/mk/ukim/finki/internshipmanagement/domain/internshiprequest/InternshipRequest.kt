package mk.ukim.finki.internshipmanagement.domain.internshiprequest
import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.*
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.events.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDate

@Aggregate
@Entity
@Table(name = "internship_request")
class InternshipRequest : LabeledEntity {

    @AggregateIdentifier
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    private lateinit var internshipRequestId: InternshipRequestId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "student_id"))
    private lateinit var studentId: StudentId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "company_id"))
    private lateinit var companyId: CompanyId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "internship_id"))
    private lateinit var internshipId: InternshipId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "coordinator_id"))
    private lateinit var coordinatorId: CoordinatorId
    private lateinit var status: InternshipRequestStatus
    private lateinit var dateOfCreation: LocalDate

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "date_of_decision"))
    private var dateOfDecision: DecisionDate? = null

    // Required by Axon
    constructor()

    @CommandHandler
    constructor(command: SubmitInternshipRequestCommand) {
        AggregateLifecycle.apply(InternshipRequestSubmittedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: InternshipRequestSubmittedEvent) {
        this.internshipRequestId = event.internshipRequestId
        this.studentId = event.studentId
        this.companyId = event.companyId
        this.internshipId = event.internshipId
        this.coordinatorId = event.coordinatorId
        this.status = InternshipRequestStatus.SUBMITTED
        this.dateOfCreation = event.dateOfCreation
    }

    override fun getId(): Identifier<out Any> = internshipRequestId
    override fun getLabel(): String = "InternshipRequest(${internshipRequestId})"

    @CommandHandler
    fun handle(command: UpdateCoordinatorCommand) {
        AggregateLifecycle.apply(CoordinatorUpdatedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: CoordinatorUpdatedEvent) {
        this.coordinatorId = event.coordinatorId
    }

    @CommandHandler
    fun handle(command: UpdateDateOfDecisionCommand) {
        AggregateLifecycle.apply(DateOfDecisionUpdatedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: DateOfDecisionUpdatedEvent) {
        this.dateOfDecision = event.dateOfDecision
    }

    @CommandHandler
    fun handle(command: UpdateInternshipIdCommand) {
        AggregateLifecycle.apply(InternshipIdUpdatedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: InternshipIdUpdatedEvent) {
        this.internshipId = event.internshipId
    }

    @CommandHandler
    fun handle(command: ApproveInternshipRequestCommand) {
        check(status != InternshipRequestStatus.REJECTED) {
            "Cannot approve an already rejected request"
        }
        check(status != InternshipRequestStatus.APPROVED) {
            "Request is already approved"
        }
        AggregateLifecycle.apply(InternshipRequestApprovedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: InternshipRequestApprovedEvent) {
        this.status = InternshipRequestStatus.APPROVED
    }

    @CommandHandler
    fun handle(command: RejectInternshipRequestCommand) {
        check(status != InternshipRequestStatus.APPROVED) {
            "Cannot reject an already approved request"
        }
        check(status != InternshipRequestStatus.REJECTED) {
            "Request is already rejected"
        }
        AggregateLifecycle.apply(InternshipRequestRejectedEvent(command))
    }

    @EventSourcingHandler
    fun on(event: InternshipRequestRejectedEvent) {
        this.status = InternshipRequestStatus.REJECTED
    }

}