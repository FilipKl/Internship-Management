package mk.ukim.finki.internshipmanagement.application.internshiprequest.query

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import org.hibernate.annotations.Immutable
import java.time.LocalDate

@Entity
@Table(name = "internship_request")
@Immutable
data class InternshipRequestView(
    @EmbeddedId
    @AttributeOverride(name = "raw", column = Column(name = "id"))
    val internshipRequestId: InternshipRequestId = InternshipRequestId(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "student_id"))
    val studentId: StudentId = StudentId(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "company_id"))
    val companyId: CompanyId = CompanyId(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "internship_id"))
    val internshipId: InternshipId = InternshipId(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "coordinator_id"))
    val coordinatorId: CoordinatorId = CoordinatorId(),

    @Enumerated(EnumType.STRING)
    val status: InternshipRequestStatus = InternshipRequestStatus.SUBMITTED,

    val dateOfCreation: LocalDate = LocalDate.now(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "date_of_decision"))
    val dateOfDecision: DecisionDate? = null
) : LabeledEntity {
    constructor() : this(
        internshipRequestId = InternshipRequestId(),
        studentId = StudentId(),
        companyId = CompanyId(),
        internshipId = InternshipId(),
        coordinatorId = CoordinatorId(),
        status = InternshipRequestStatus.SUBMITTED,
        dateOfCreation = LocalDate.now(),
        dateOfDecision = null
    )

    override fun getId(): Identifier<out Any> = internshipRequestId
    override fun getLabel(): String = "InternshipRequest($internshipRequestId)"
}