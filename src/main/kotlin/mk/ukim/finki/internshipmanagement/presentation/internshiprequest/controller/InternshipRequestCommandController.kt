package mk.ukim.finki.internshipmanagement.presentation.internshiprequest.controller

import mk.ukim.finki.internshipmanagement.application.internshiprequest.SubmitInternshipRequestService
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.StudentId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.ApproveInternshipRequestCommand
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.RejectInternshipRequestCommand
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.SubmitInternshipRequestCommand
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateCoordinatorCommand
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateDateOfDecisionCommand
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.commands.UpdateInternshipIdCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

/**
 * REST controller for InternshipRequest write-side operations.
 */
@RestController
@RequestMapping("/api/v1/internship-requests")
class InternshipRequestCommandController(
    private val submitInternshipRequestService: SubmitInternshipRequestService,
    private val commandGateway: CommandGateway
) {

    @PostMapping
    fun submitRequest(@RequestBody request: SubmitInternshipRequestRequest): ResponseEntity<Map<String, String>> {
        val command = SubmitInternshipRequestCommand(
            studentId = StudentId(request.studentId),
            companyId = CompanyId(request.companyId),
            internshipId = InternshipId(request.internshipId),
            coordinatorId = CoordinatorId(request.coordinatorId),
            dateOfCreation = request.dateOfCreation ?: LocalDate.now()
        )

        return try {
            submitInternshipRequestService.submitWithValidation(command)
            ResponseEntity.ok(
                mapOf(
                    "internshipRequestId" to command.internshipRequestId.toString(),
                    "status" to "SUBMITTED"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to submit internship request")))
        }
    }

    @PostMapping("/{id}/approve")
    fun approve(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        val command = ApproveInternshipRequestCommand(
            internshipRequestId = InternshipRequestId(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship request approved", "requestStatus" to "APPROVED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to approve internship request")))
        }
    }

    @PostMapping("/{id}/reject")
    fun reject(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        val command = RejectInternshipRequestCommand(
            internshipRequestId = InternshipRequestId(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship request rejected", "requestStatus" to "REJECTED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to reject internship request")))
        }
    }

    @PostMapping("/{id}/coordinator")
    fun updateCoordinator(
        @PathVariable id: String,
        @RequestBody request: UpdateCoordinatorRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateCoordinatorCommand(
            internshipRequestId = InternshipRequestId(id),
            coordinatorId = CoordinatorId(request.coordinatorId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Coordinator updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update coordinator")))
        }
    }

    @PostMapping("/{id}/internship")
    fun updateInternship(
        @PathVariable id: String,
        @RequestBody request: UpdateInternshipRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateInternshipIdCommand(
            internshipRequestId = InternshipRequestId(id),
            internshipId = InternshipId(request.internshipId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update internship")))
        }
    }

    @PostMapping("/{id}/decision-date")
    fun updateDecisionDate(
        @PathVariable id: String,
        @RequestBody request: UpdateDecisionDateRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateDateOfDecisionCommand(
            internshipRequestId = InternshipRequestId(id),
            dateOfDecision = DecisionDate(request.dateOfDecision)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Decision date updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update decision date")))
        }
    }
}

data class SubmitInternshipRequestRequest(
    val studentId: String,
    val companyId: String,
    val internshipId: String,
    val coordinatorId: String,
    val dateOfCreation: LocalDate? = null
)

data class UpdateCoordinatorRequest(
    val coordinatorId: String
)

data class UpdateInternshipRequest(
    val internshipId: String
)

data class UpdateDecisionDateRequest(
    val dateOfDecision: LocalDate
)

