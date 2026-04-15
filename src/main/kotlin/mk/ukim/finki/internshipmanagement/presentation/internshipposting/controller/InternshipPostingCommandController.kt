package mk.ukim.finki.internshipmanagement.presentation.internshipposting.controller

import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * REST Controller for InternshipPosting write operations (commands).
 * Handles all command requests that modify the InternshipPosting aggregate state.
 * Commands are sent to Axon Server which processes them and emits events.
 */
@RestController
@RequestMapping("/api/v1/internship-postings")
class InternshipPostingCommandController(
    private val commandGateway: CommandGateway
) {

    /**
     * Create a new InternshipPosting.
     * POST /api/v1/internship-postings
     *
     * Command Flow:
     * CreateInternshipPostingCommand → InternshipPostingCreatedEvent → Read Model Updated
     *
     * @param request Contains posting details (title, company, description, dates, location, techStack, contactEmail)
     * @return Created posting ID
     */
    @PostMapping
    fun createPosting(@RequestBody request: CreateInternshipPostingRequest): ResponseEntity<Map<String, String>> {
        val postingId = InternshipPostingId.generate()
        val command = CreateInternshipPostingCommand(
            id = postingId,
            title = request.title,
            company = request.company,
            description = request.description,
            postedDate = request.postedDate,
            validUntil = request.validUntil,
            location = request.location,
            techStack = request.techStack,
            contactEmail = request.contactEmail
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("postingId" to postingId.toString(), "status" to "DRAFT"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to create posting")))
        }
    }

    /**
     * Update an existing InternshipPosting.
     * PUT /api/v1/internship-postings/{id}
     *
     * Command Flow:
     * UpdateInternshipPostingCommand → InternshipPostingUpdatedEvent → Read Model Updated
     *
     * @param id The InternshipPosting ID
     * @param request Contains updated posting details
     * @return Success or error message
     */
    @PutMapping("/{id}")
    fun updatePosting(
        @PathVariable id: String,
        @RequestBody request: UpdateInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateInternshipPostingCommand(
            id = InternshipPostingId.from(id),
            title = request.title,
            company = request.company,
            description = request.description,
            validUntil = request.validUntil,
            location = request.location,
            techStack = request.techStack,
            contactEmail = request.contactEmail
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update posting")))
        }
    }

    /**
     * Edit an InternshipPosting (limited fields).
     * PATCH /api/v1/internship-postings/{id}/edit
     *
     * Command Flow:
     * EditInternshipPostingCommand → InternshipPostingEditedEvent → Read Model Updated
     *
     * @param id The InternshipPosting ID
     * @param request Contains title, description, and techStack
     * @return Success or error message
     */
    @PatchMapping("/{id}/edit")
    fun editPosting(
        @PathVariable id: String,
        @RequestBody request: EditInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        val command = EditInternshipPostingCommand(
            id = InternshipPostingId.from(id),
            title = request.title,
            description = request.description,
            techStack = request.techStack
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting edited successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to edit posting")))
        }
    }

    /**
     * Publish an InternshipPosting.
     * POST /api/v1/internship-postings/{id}/publish
     *
     * Command Flow:
     * PublishInternshipPostingCommand → InternshipPostingPublishedEvent → Posting status changes to PUBLISHED
     *
     * @param id The InternshipPosting ID
     * @param request Contains publishedBy (user ID)
     * @return Success or error message
     */
    @PostMapping("/{id}/publish")
    fun publishPosting(
        @PathVariable id: String,
        @RequestBody request: PublishInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        val command = PublishInternshipPostingCommand(
            id = InternshipPostingId.from(id),
            publishedBy = request.publishedBy
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting published successfully", "postingStatus" to "PUBLISHED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to publish posting")))
        }
    }

    /**
     * Delete an InternshipPosting.
     * DELETE /api/v1/internship-postings/{id}
     *
     * Command Flow:
     * DeleteInternshipPostingCommand → InternshipPostingDeletedEvent → Posting is removed
     *
     * @param id The InternshipPosting ID
     * @return Success or error message
     */
    @DeleteMapping("/{id}")
    fun deletePosting(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        val command = DeleteInternshipPostingCommand(
            id = InternshipPostingId.from(id)
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting deleted successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to delete posting")))
        }
    }
}

// ====== Request DTOs ======

/**
 * Request DTO for creating a new InternshipPosting
 */
data class CreateInternshipPostingRequest(
    val title: String,
    val company: String,
    val description: String,
    val postedDate: LocalDate,
    val validUntil: LocalDate,
    val location: String,
    val techStack: String,
    val contactEmail: String
)

/**
 * Request DTO for updating an InternshipPosting
 */
data class UpdateInternshipPostingRequest(
    val title: String,
    val company: String,
    val description: String,
    val validUntil: LocalDate,
    val location: String,
    val techStack: String,
    val contactEmail: String
)

/**
 * Request DTO for editing an InternshipPosting (limited fields)
 */
data class EditInternshipPostingRequest(
    val title: String,
    val description: String,
    val techStack: String
)

/**
 * Request DTO for publishing an InternshipPosting
 */
data class PublishInternshipPostingRequest(
    val publishedBy: String
)

