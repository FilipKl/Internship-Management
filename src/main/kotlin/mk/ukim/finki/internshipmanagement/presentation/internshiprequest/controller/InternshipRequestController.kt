package mk.ukim.finki.internshipmanagement.presentation.internshiprequest.controller

import mk.ukim.finki.internshipmanagement.application.internshiprequest.query.InternshipRequestQueryService
import mk.ukim.finki.internshipmanagement.application.internshiprequest.query.InternshipRequestView
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/internship-requests")
class InternshipRequestController(
    private val queryService: InternshipRequestQueryService
) {

    @GetMapping("/all")
    fun findAll(): List<InternshipRequestView> {
        return queryService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): InternshipRequestView {
        return queryService.findById(InternshipRequestId(id))
    }

    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: InternshipRequestStatus): List<InternshipRequestView> {
        return queryService.findByStatus(status)
    }

    @GetMapping("/by-student/{studentId}")
    fun findByStudentId(@PathVariable studentId: String): List<InternshipRequestView> {
        return queryService.findByStudentId(studentId)
    }

    @GetMapping("/by-coordinator/{coordinatorId}")
    fun findByCoordinatorId(@PathVariable coordinatorId: String): List<InternshipRequestView> {
        return queryService.findByCoordinatorId(coordinatorId)
    }
}