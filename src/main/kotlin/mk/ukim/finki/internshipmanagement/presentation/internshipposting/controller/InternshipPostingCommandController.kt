package mk.ukim.finki.internshipmanagement.presentation.internshipposting.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import jakarta.validation.Valid
import jakarta.validation.constraints.*


@RestController
@RequestMapping("/api/v1/internship-postings")
@Tag(
    name = "Internship Posting Commands",
    description = "API for creating and modifying internship posting data. Submit commands to create, update, publish, or delete postings."
)
class InternshipPostingCommandController(
    private val commandGateway: CommandGateway
) {

    @Operation(
        summary = "Create a new internship posting",
        description = "Submits a CreateInternshipPostingCommand to create a new internship posting in DRAFT status."
    )
    @PostMapping
    fun createPosting(@Valid @RequestBody request: CreateInternshipPostingRequest): ResponseEntity<Map<String, String>> {
        return try {
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
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("postingId" to postingId.toString(), "status" to "DRAFT"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Validation failed")))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Failed to create posting")))
        }
    }

    @Operation(
        summary = "Update an internship posting",
        description = "Updates an existing internship posting with new details (title, company, description, location, tech stack, contact email)."
    )
    @PutMapping("/{id}")
    fun updatePosting(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        return try {
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
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting updated successfully"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Validation failed")))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Failed to update posting")))
        }
    }

    @Operation(
        summary = "Edit an internship posting (limited fields)",
        description = "Edits specific fields (title, description, technology stack) of an existing internship posting."
    )
    @PatchMapping("/{id}/edit")
    fun editPosting(
        @PathVariable id: String,
        @Valid @RequestBody request: EditInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        return try {
            val command = EditInternshipPostingCommand(
                id = InternshipPostingId.from(id),
                title = request.title,
                description = request.description,
                techStack = request.techStack
            )
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting edited successfully"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Validation failed")))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Failed to edit posting")))
        }
    }

    @Operation(
        summary = "Publish an internship posting",
        description = "Publishes an internship posting, making it visible to students. Changes status from DRAFT to PUBLISHED."
    )
    @PostMapping("/{id}/publish")
    fun publishPosting(
        @PathVariable id: String,
        @Valid @RequestBody request: PublishInternshipPostingRequest
    ): ResponseEntity<Map<String, String>> {
        return try {
            val command = PublishInternshipPostingCommand(
                id = InternshipPostingId.from(id),
                publishedBy = request.publishedBy
            )
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting published successfully", "postingStatus" to "PUBLISHED"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Validation failed")))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Failed to publish posting")))
        }
    }

    @Operation(
        summary = "Delete an internship posting",
        description = "Deletes an internship posting from the system. Once deleted, the posting is no longer visible to students."
    )
    @DeleteMapping("/{id}")
    fun deletePosting(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        return try {
            val command = DeleteInternshipPostingCommand(
                id = InternshipPostingId.from(id)
            )
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Posting deleted successfully"))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Validation failed")))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (e.message ?: "Failed to delete posting")))
        }
    }
}


data class CreateInternshipPostingRequest(
    @field:NotBlank(message = "Title cannot be blank")
    @field:Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    val title: String,

    @field:NotBlank(message = "Company name cannot be blank")
    @field:Size(max = 255, message = "Company name cannot exceed 255 characters")
    val company: String,

    @field:NotBlank(message = "Description cannot be blank")
    @field:Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    val description: String,

    @field:NotNull(message = "Posted date is required")
    val postedDate: LocalDate,

    @field:NotNull(message = "Valid until date is required")
    val validUntil: LocalDate,

    @field:NotBlank(message = "Location cannot be blank")
    val location: String,

    @field:NotBlank(message = "Tech stack cannot be blank")
    @field:Size(max = 1000, message = "Tech stack cannot exceed 1000 characters")
    val techStack: String,

    @field:NotBlank(message = "Contact email cannot be blank")
    @field:Email(message = "Contact email must be a valid email address")
    val contactEmail: String
)


data class UpdateInternshipPostingRequest(
    @field:NotBlank(message = "Title cannot be blank")
    @field:Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    val title: String,

    @field:NotBlank(message = "Company name cannot be blank")
    @field:Size(max = 255, message = "Company name cannot exceed 255 characters")
    val company: String,

    @field:NotBlank(message = "Description cannot be blank")
    @field:Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    val description: String,

    @field:NotNull(message = "Valid until date is required")
    val validUntil: LocalDate,

    @field:NotBlank(message = "Location cannot be blank")
    val location: String,

    @field:NotBlank(message = "Tech stack cannot be blank")
    @field:Size(max = 1000, message = "Tech stack cannot exceed 1000 characters")
    val techStack: String,

    @field:NotBlank(message = "Contact email cannot be blank")
    @field:Email(message = "Contact email must be a valid email address")
    val contactEmail: String
)


data class EditInternshipPostingRequest(
    @field:NotBlank(message = "Title cannot be blank")
    @field:Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    val title: String,

    @field:NotBlank(message = "Description cannot be blank")
    @field:Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    val description: String,

    @field:NotBlank(message = "Tech stack cannot be blank")
    @field:Size(max = 1000, message = "Tech stack cannot exceed 1000 characters")
    val techStack: String
)

data class PublishInternshipPostingRequest(
    @field:NotBlank(message = "Publisher ID cannot be blank")
    val publishedBy: String
)

