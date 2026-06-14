package mk.ukim.finki.internshipmanagement.application.InternshipJournal.query

import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.InternshipJournalStatus
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface InternshipJournalViewJpaRepository :
    JpaRepository<InternshipJournalView, InternshipJournalId> {

    fun findByStatus(status: InternshipJournalStatus): List<InternshipJournalView>

    fun findByStudentId(studentId: StudentId): List<InternshipJournalView>

    fun findByDateCreatedBetween(from: LocalDateTime, to: LocalDateTime): List<InternshipJournalView>
}