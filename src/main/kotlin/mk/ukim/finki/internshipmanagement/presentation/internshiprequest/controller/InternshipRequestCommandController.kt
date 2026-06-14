package mk.ukim.finki.internshipmanagement.presentation.internshiprequest.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CompanyId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.CoordinatorId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.DecisionDate
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipId
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.InternshipRequestId
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.StudentId
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

@RestController
@RequestMapping("/api/v1/internship-requests")
@Tag(
    name = "Internship Request Commands",
    description = "API for creating and modifying internship request data. Submit requests, approve, reject, and update request details."
)
class InternshipRequestCommandController(
    private val commandGateway: CommandGateway
) {

    private fun toClientId(id: InternshipRequestId): String =
        id.toString()

    private fun toAggregateId(id: String): InternshipRequestId =
        InternshipRequestId.from(id)

    @Operation(
        summary = "Submit a new internship request",
        description = "Submits a new internship request from a student to a coordinator for approval."
    )
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
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(
                mapOf(
                    "internshipRequestId" to toClientId(command.internshipRequestId),
                    "status" to "SUBMITTED"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to submit internship request")))
        }
    }

    @Operation(
        summary = "Approve an internship request",
        description = "Approves a pending internship request, changing its status from SUBMITTED to APPROVED."
    )
    @PostMapping("/{id}/approve")
    fun approve(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        val command = ApproveInternshipRequestCommand(
            internshipRequestId = toAggregateId(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship request approved", "requestStatus" to "APPROVED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to approve internship request")))
        }
    }

    @Operation(
        summary = "Reject an internship request",
        description = "Rejects a pending internship request, changing its status from SUBMITTED to REJECTED."
    )
    @PostMapping("/{id}/reject")
    fun reject(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        val command = RejectInternshipRequestCommand(
            internshipRequestId = toAggregateId(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship request rejected", "requestStatus" to "REJECTED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to reject internship request")))
        }
    }

    @Operation(
        summary = "Update the assigned coordinator",
        description = "Updates the coordinator assigned to an internship request."
    )
    @PostMapping("/{id}/coordinator")
    fun updateCoordinator(
        @PathVariable id: String,
        @RequestBody request: UpdateCoordinatorRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateCoordinatorCommand(
            internshipRequestId = toAggregateId(id),
            coordinatorId = CoordinatorId(request.coordinatorId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Coordinator updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update coordinator")))
        }
    }

    @Operation(
        summary = "Update the internship ID",
        description = "Updates the internship ID associated with an internship request."
    )
    @PostMapping("/{id}/internship")
    fun updateInternship(
        @PathVariable id: String,
        @RequestBody request: UpdateInternshipRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateInternshipIdCommand(
            internshipRequestId = toAggregateId(id),
            internshipId = InternshipId(request.internshipId)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Internship updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update internship")))
        }
    }

    @Operation(
        summary = "Update the decision date",
        description = "Updates the date when the coordinator made the approval or rejection decision."
    )
    @PostMapping("/{id}/decision-date")
    fun updateDecisionDate(
        @PathVariable id: String,
        @RequestBody request: UpdateDecisionDateRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateDateOfDecisionCommand(
            internshipRequestId = toAggregateId(id),
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

