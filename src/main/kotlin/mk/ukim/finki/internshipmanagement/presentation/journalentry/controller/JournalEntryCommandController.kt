package mk.ukim.finki.internshipmanagement.presentation.journalentry.controller

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
class JournalEntryCommandController(
    private val commandGateway: CommandGateway
) {

    /**
     * Create a new JournalEntry (Add Weekly Entry).
     * POST /api/v1/journal-entries
     *
     * Command Flow:
     * CreateJournalEntryCommand → JournalEntryCreatedEvent → Read Model Updated
     *
     * @param request Contains journalId, titleText, and contentText
     * @return Created entry ID
     */
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

    /**
     * Update JournalEntry title (Edit Entry).
     * PATCH /api/v1/journal-entries/{id}/title
     *
     * Command Flow:
     * UpdateEntryTitleCommand → EntryTitleUpdatedEvent → Read Model Updated
     *
     * Note: Only DRAFT entries can be edited
     *
     * @param id The JournalEntry ID
     * @param request Contains new title text
     * @return Success or error message
     */
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

    /**
     * Update JournalEntry content (Edit Entry).
     * PATCH /api/v1/journal-entries/{id}/content
     *
     * Command Flow:
     * UpdateEntryContentCommand → EntryContentUpdatedEvent → Read Model Updated
     *
     * Note: Only DRAFT entries can be edited
     *
     * @param id The JournalEntry ID
     * @param request Contains new content text
     * @return Success or error message
     */
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

    /**
     * Validate a JournalEntry (Validate Entry).
     * POST /api/v1/journal-entries/{id}/validate
     *
     * Command Flow:
     * ValidateJournalEntryCommand → JournalEntryValidatedEvent → Entry status changes to VALIDATED
     *
     * Note: Only DRAFT entries can be validated
     *
     * @param id The JournalEntry ID
     * @param request Contains validatedBy (reviewer ID)
     * @return Success or error message
     */
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

    /**
     * Reject a JournalEntry (Reject Entry).
     * POST /api/v1/journal-entries/{id}/reject
     *
     * Command Flow:
     * RejectJournalEntryCommand → JournalEntryRejectedEvent → Entry status changes to REJECTED
     *                                                       → Event handler notifies student (policy)
     *
     * Note: Only DRAFT entries can be rejected
     *
     * @param id The JournalEntry ID
     * @param request Contains rejectedBy (reviewer ID) and rejectionReason
     * @return Success or error message
     */
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


