package mk.ukim.finki.internshipmanagement.presentation.journalentry.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import mk.ukim.finki.internshipmanagement.domain.journalentry.commands.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

/**
 * REST Controller for JournalEntry write operations (commands).
 * Handles all command requests that modify the JournalEntry aggregate state.
 * Commands are sent to Axon Server which processes them and emits events.
 */
@RestController
@RequestMapping("/api/v1/journal-entries")
@Tag(
    name = "Journal Entry Commands",
    description = "API for creating and modifying journal entry data. Create, edit, validate, and reject weekly internship journal entries."
)
class JournalEntryCommandController(
    private val commandGateway: CommandGateway
) {

    @Operation(
        summary = "Create a new journal entry",
        description = "Creates a new weekly journal entry within an internship journal."
    )
    @PostMapping
    fun createEntry(@RequestBody request: CreateJournalEntryRequest): ResponseEntity<Map<String, String>> {
        val entryId = JournalEntryId.generate()
        val command = CreateJournalEntryCommand(
            id = entryId,
            journalId = request.journalId,
            titleText = request.titleText,
            contentText = request.contentText
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("entryId" to entryId.toString(), "status" to "DRAFT"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to create entry")))
        }
    }

    @Operation(
        summary = "Update entry title",
        description = "Updates the title of a journal entry. Only DRAFT entries can be edited."
    )
    @PatchMapping("/{id}/title")
    fun updateTitle(
        @PathVariable id: String,
        @RequestBody request: UpdateTitleRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateEntryTitleCommand(
            entryId = JournalEntryId.from(id),
            newTitleText = request.newTitleText
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Title updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update title")))
        }
    }

    @Operation(
        summary = "Update entry content",
        description = "Updates the content of a journal entry. Only DRAFT entries can be edited."
    )
    @PatchMapping("/{id}/content")
    fun updateContent(
        @PathVariable id: String,
        @RequestBody request: UpdateContentRequest
    ): ResponseEntity<Map<String, String>> {
        val command = UpdateEntryContentCommand(
            entryId = JournalEntryId.from(id),
            newContentText = request.newContentText
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Content updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to update content")))
        }
    }

    @Operation(
        summary = "Validate a journal entry",
        description = "Validates a journal entry, changing its status from DRAFT to VALIDATED. Only DRAFT entries can be validated."
    )
    @PostMapping("/{id}/validate")
    fun validateEntry(
        @PathVariable id: String,
        @RequestBody request: ValidateEntryRequest
    ): ResponseEntity<Map<String, String>> {
        val command = ValidateJournalEntryCommand(
            entryId = JournalEntryId.from(id),
            validatedBy = request.validatedBy
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Entry validated successfully", "entryStatus" to "VALIDATED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to validate entry")))
        }
    }

    @Operation(
        summary = "Reject a journal entry",
        description = "Rejects a journal entry, changing its status from DRAFT to REJECTED. Only DRAFT entries can be rejected."
    )
    @PostMapping("/{id}/reject")
    fun rejectEntry(
        @PathVariable id: String,
        @RequestBody request: RejectEntryRequest
    ): ResponseEntity<Map<String, String>> {
        val command = RejectJournalEntryCommand(
            entryId = JournalEntryId.from(id),
            rejectedBy = request.rejectedBy,
            rejectionReason = request.rejectionReason
        )

        return try {
            commandGateway.sendAndWait<Any>(command)
            ResponseEntity.ok(mapOf("status" to "Entry rejected successfully", "entryStatus" to "REJECTED"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Failed to reject entry")))
        }
    }
}

// ====== Request DTOs ======

/**
 * Request DTO for creating a new JournalEntry
 */
data class CreateJournalEntryRequest(
    val journalId: String,
    val titleText: String,
    val contentText: String
)

/**
 * Request DTO for updating entry title
 */
data class UpdateTitleRequest(
    val newTitleText: String
)

/**
 * Request DTO for updating entry content
 */
data class UpdateContentRequest(
    val newContentText: String
)

/**
 * Request DTO for validating an entry
 */
data class ValidateEntryRequest(
    val validatedBy: String
)

/**
 * Request DTO for rejecting an entry
 */
data class RejectEntryRequest(
    val rejectedBy: String,
    val rejectionReason: String
)


