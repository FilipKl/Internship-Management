package mk.ukim.finki.internshipmanagement.application.internshiprequest.query

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import org.hibernate.annotations.Immutable
import java.time.LocalDate

@Entity
@Table(name = "internship_request")
@Immutable
data class InternshipRequestView(
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val internshipRequestId: InternshipRequestId,

    val studentId: String,
    val companyId: String,
    val internshipId: String,
    val coordinatorId: String,

    @Enumerated(EnumType.STRING)
    val status: InternshipRequestStatus,

    val dateOfCreation: LocalDate,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "date_of_decision"))
    val dateOfDecision: DecisionDate? = null
) : LabeledEntity {
    override fun getId(): Identifier<out Any> = internshipRequestId
    override fun getLabel(): String = "InternshipRequest($internshipRequestId)"
}