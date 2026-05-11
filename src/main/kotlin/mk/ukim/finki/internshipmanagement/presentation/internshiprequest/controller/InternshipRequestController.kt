package mk.ukim.finki.internshipmanagement.presentation.internshiprequest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.application.internshiprequest.query.InternshipRequestQueryService
import mk.ukim.finki.internshipmanagement.application.internshiprequest.query.InternshipRequestView
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestStatus
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/internship-requests")
@Tag(
    name = "Internship Request Query API",
    description = "API for querying internship request data. Browse requests by ID, status, student, or coordinator."
)
class InternshipRequestController(
    private val queryService: InternshipRequestQueryService
) {

    @Operation(
        summary = "Get all internship requests",
        description = "Retrieves all internship requests in the system."
    )
    @GetMapping("/all")
    fun findAll(): List<InternshipRequestView> {
        return queryService.findAll()
    }

    @Operation(
        summary = "Get internship request by ID",
        description = "Retrieves a specific internship request by its unique identifier."
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): InternshipRequestView {
        return queryService.findById(InternshipRequestId.from(id))
    }

    @Operation(
        summary = "Find requests by status",
        description = "Retrieves all internship requests filtered by their approval status."
    )
    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: InternshipRequestStatus): List<InternshipRequestView> {
        return queryService.findByStatus(status)
    }

    @Operation(
        summary = "Find requests by student ID",
        description = "Retrieves all internship requests submitted by a specific student."
    )
    @GetMapping("/by-student/{studentId}")
    fun findByStudentId(@PathVariable studentId: String): List<InternshipRequestView> {
        return queryService.findByStudentId(StudentId(studentId))
    }

    @Operation(
        summary = "Find requests by coordinator ID",
        description = "Retrieves all internship requests assigned to a specific coordinator for approval."
    )
    @GetMapping("/by-coordinator/{coordinatorId}")
    fun findByCoordinatorId(@PathVariable coordinatorId: String): List<InternshipRequestView> {
        return queryService.findByCoordinatorId(CoordinatorId(coordinatorId))
    }
}