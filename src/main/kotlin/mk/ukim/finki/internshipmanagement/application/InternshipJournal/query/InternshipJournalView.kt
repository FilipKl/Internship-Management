package mk.ukim.finki.internshipmanagement.application.InternshipJournal.query

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.CompanyName
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalStatus
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.ProfessorId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

@Entity
@Table(name = "internship_journal") // same table as the aggregate
@Immutable
data class InternshipJournalView(


    @Id
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val internshipJournalId: InternshipJournalId = InternshipJournalId(),

    @Embedded
    val companyName: CompanyName,

    @Embedded
    val studentId: StudentId,

    @Embedded
    val professorId: ProfessorId,

    @Enumerated(EnumType.STRING)
    val status: InternshipJournalStatus,

    val dateCreated: LocalDateTime,

    val numberOfEntries: Int // derived from entries, for display
    ,
    @Id
    var id: Long? = null
) : LabeledEntity {

    override fun getId(): Identifier<out Any> = internshipJournalId

    override fun getLabel(): String = "Internship of student ${studentId} at ${companyName}"
}