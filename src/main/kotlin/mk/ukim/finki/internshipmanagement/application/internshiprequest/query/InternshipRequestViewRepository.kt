package mk.ukim.finki.internshipmanagement.application.internshiprequest.query

import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipRequestViewRepository
    : JpaRepository<InternshipRequestView, InternshipRequestId> {

    fun findByStatus(status: InternshipRequestStatus): List<InternshipRequestView>

    fun findByStudentId(studentId: String): List<InternshipRequestView>

    fun findByCoordinatorId(coordinatorId: String): List<InternshipRequestView>
}