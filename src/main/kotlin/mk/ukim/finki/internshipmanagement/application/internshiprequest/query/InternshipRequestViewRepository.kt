package mk.ukim.finki.internshipmanagement.application.internshiprequest.query

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipRequestViewRepository
    : JpaRepository<InternshipRequestView, InternshipRequestId> {

    fun findByStatus(status: InternshipRequestStatus): List<InternshipRequestView>

    fun findByStudentId(studentId: StudentId): List<InternshipRequestView>

    fun findByCoordinatorId(coordinatorId: CoordinatorId): List<InternshipRequestView>
}