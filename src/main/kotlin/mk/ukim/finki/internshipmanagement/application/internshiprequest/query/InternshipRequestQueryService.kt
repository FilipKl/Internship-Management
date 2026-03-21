package mk.ukim.finki.internshipmanagement.application.internshiprequest.query

import mk.ukim.finki.internshipmanagement.application.internshiprequest.exception.InternshipRequestNotFoundException
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import org.springframework.stereotype.Service

interface InternshipRequestQueryService {
    fun findAll(): List<InternshipRequestView>
    fun findById(id: InternshipRequestId): InternshipRequestView
    fun findByStatus(status: InternshipRequestStatus): List<InternshipRequestView>
    fun findByStudentId(studentId: String): List<InternshipRequestView>
    fun findByCoordinatorId(coordinatorId: String): List<InternshipRequestView>
}

@Service
class InternshipRequestQueryServiceImpl(
    private val repository: InternshipRequestViewRepository
) : InternshipRequestQueryService {

    override fun findAll(): List<InternshipRequestView> {
        return repository.findAll()
    }

    override fun findById(id: InternshipRequestId): InternshipRequestView {
        return repository.findById(id)
            .orElseThrow { InternshipRequestNotFoundException(id) }
    }

    override fun findByStatus(status: InternshipRequestStatus): List<InternshipRequestView> {
        return repository.findByStatus(status)
    }

    override fun findByStudentId(studentId: String): List<InternshipRequestView> {
        return repository.findByStudentId(studentId)
    }

    override fun findByCoordinatorId(coordinatorId: String): List<InternshipRequestView> {
        return repository.findByCoordinatorId(coordinatorId)
    }
}

