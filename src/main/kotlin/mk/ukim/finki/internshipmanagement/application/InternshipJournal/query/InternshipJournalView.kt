package mk.ukim.finki.internshipmanagement.application.InternshipJournal.query

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.Transient
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


    @EmbeddedId
    @AttributeOverride(name = "id", column = Column(name = "id"))
    val internshipJournalId: InternshipJournalId = InternshipJournalId(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "company_name"))
    val companyName: CompanyName,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "student_id"))
    val studentId: StudentId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "professor_id"))
    val professorId: ProfessorId,

    @Enumerated(EnumType.STRING)
    val status: InternshipJournalStatus,

    val dateCreated: LocalDateTime,

    @Transient
    val numberOfEntries: Int = 0 // derived field, not persisted in internship_journal
) : LabeledEntity {

    override fun getId(): Identifier<out Any> = internshipJournalId

    override fun getLabel(): String = "Internship of student ${studentId} at ${companyName}"
}