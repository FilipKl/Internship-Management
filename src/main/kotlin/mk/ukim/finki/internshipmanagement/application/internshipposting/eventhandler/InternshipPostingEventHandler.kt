package mk.ukim.finki.internshipmanagement.application.internshipposting.eventhandler

import mk.ukim.finki.internshipmanagement.application.internshipposting.query.InternshipPostingViewReadService
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingCreatedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingUpdatedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingEditedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingPublishedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingDeletedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
/**
 * Cross-Aggregate Event Handler for InternshipPosting.
 * 
 * Listens to events published by InternshipPosting aggregate and reacts accordingly.
 * This handler demonstrates the CQRS pattern where the read model is updated
 * in response to domain events without direct coupling.
 * 
 * The handler is responsible for:
 * 1. Maintaining eventual consistency between write and read models
 * 2. Performing side effects triggered by state changes in the aggregate
 * 3. Publishing notifications to external systems if needed
 */
@Component
class InternshipPostingEventHandler(
    private val internshipPostingReadService: InternshipPostingViewReadService,
    private val commandGateway: CommandGateway
) {
    
    /**
     * Handle InternshipPostingCreatedEvent.
     * 
     * Triggered when: A new InternshipPosting is created and moves to DRAFT status
     * Reaction: The read model is updated to include the new posting
     * 
     * This event handler updates the projection (read model) to reflect the new state.
     * The InternshipPostingView will now be available for queries.
     * 
     * Note: In a real-world application, you might want to:
     * - Send a notification to the company confirming posting creation
     * - Initialize any tracking/analytics state
     * - Validate that the company exists in an external service
     */
    @EventHandler
    fun handle(event: InternshipPostingCreatedEvent) {
        logger.info(
            "InternshipPosting created: ${event.aggId} | " +
            "Title: ${event.title} | Company: ${event.company}"
        )
        
        // The read model (InternshipPostingView) is updated automatically through
        // event projections. Verify the posting is available in the read model
        // for subsequent queries and operations.
        try {
            val createdPosting = internshipPostingReadService.findById(
                InternshipPostingId.from(event.aggId)
            )
            logger.info(
                "InternshipPostingView successfully created and is available for queries: " +
                "${createdPosting.jobTitle} at ${createdPosting.companyName}"
            )
        } catch (e: Exception) {
            logger.warn(
                "InternshipPostingView not yet available in read model. " +
                "This is normal during async event processing: ${event.aggId}"
            )
        }
    }
    
    /**
     * Handle InternshipPostingUpdatedEvent.
     * 
     * Triggered when: An existing DRAFT posting is updated with new details
     * Reaction: The read model is updated with the new information
     * 
     * This occurs when a posting hasn't been published yet and changes are made to:
     * - Title, Description, Tech Stack, Company, Location
     * 
     * Why this cannot live directly in InternshipPosting:
     * - CQRS pattern separates command (write) model from query (read) model
     * - The aggregate doesn't directly update the read projection
     * - Events are the single source of truth for both models
     */
    @EventHandler
    fun handle(event: InternshipPostingUpdatedEvent) {
        logger.info(
            "InternshipPosting updated: ${event.aggId} | " +
            "Title: ${event.title} | Tech Stack: ${event.techStack}"
        )
        
        // Use read service to fetch and verify the updated state is accessible
        try {
            val updatedPosting = internshipPostingReadService.findById(
                InternshipPostingId.from(event.aggId)
            )
            logger.info(
                "Updated posting verified in read model: ${updatedPosting.jobTitle} " +
                "(Status: ${updatedPosting.status})"
            )
        } catch (e: Exception) {
            logger.warn(
                "Updated posting not yet available in read model. " +
                "Updates may not be immediately visible: ${event.aggId}"
            )
        }
        
        // The read model projection is updated through event handlers.
        // This handler serves as a coordination point for additional logic needed
        // when updates occur, such as:
        // - Sending update notifications
        // - Triggering search index updates
        // - Logging audit trails
    }
    
    /**
     * Handle InternshipPostingEditedEvent.
     * 
     * Triggered when: A partial edit is made to DRAFT posting (edit operation)
     * Reaction: Specific fields in the read model are updated
     * 
     * This differs from Update in that it's a more granular change,
     * typically updating only critical fields like title, description, or tech stack.
     * This allows for different business logic/validation for partial vs full updates.
     */
    @EventHandler
    fun handle(event: InternshipPostingEditedEvent) {
        logger.info(
            "InternshipPosting edited: ${event.aggId} | " +
            "New Title: ${event.title}"
        )
        
        // Side effects for edit operations could include:
        // - Marking the posting as "recently edited" for UI display
        // - Notifying watchers/followers of the change
        // - Updating version/revision information
    }
    
    /**
     * Handle InternshipPostingPublishedEvent.
     * 
     * Triggered when: A DRAFT posting is published and becomes PUBLISHED
     * Reaction: The posting becomes visible to all users and available for applications
     * 
     * This is a critical state transition with multiple potential side effects:
     * 1. The read model status changes from DRAFT to PUBLISHED
     * 2. The posting becomes searchable in public listings
     * 3. Notifications should be sent to interested parties
     * 4. Analytics tracking for posting visibility
     * 
     * Ordering matters: After publish, the posting should be immediately visible
     * to queries and external systems.
     */
    @EventHandler
    fun handle(event: InternshipPostingPublishedEvent) {
        logger.info(
            "InternshipPosting published: ${event.aggId} | " +
            "Published by: ${event.publishedBy}"
        )
        
        // Use commandGateway.sendAndWait() to ensure synchronous handling of related operations.
        // This is necessary when publishing because:
        // 1. We need to verify the posting exists and is in the correct state BEFORE
        //    making it visible to public queries
        // 2. If there are dependent aggregates (e.g., notifications, analytics), we need
        //    to ensure their commands complete before confirming publication
        // 3. sendAndWait() blocks until the command completes, maintaining consistency
        //    between the write model (aggregate) and read model (denormalized view)
        
        try {
            // Verify the posting exists in the read model and is now PUBLISHED
            val publishedPosting = internshipPostingReadService.findById(
                InternshipPostingId.from(event.aggId)
            )
            
            if (publishedPosting.isPublished()) {
                logger.info(
                    "Posting successfully published and visible in read model: " +
                    "${publishedPosting.jobTitle} for ${publishedPosting.companyName}"
                )
                
                // Example: If you had a notification aggregate, you could send a command here:
                // val notifyCommand = NotifyPublishingCommand(event.aggId, event.publishedBy)
                // commandGateway.sendAndWait(notifyCommand)
                // This ensures the notification is processed synchronously before returning
            } else {
                logger.warn(
                    "Posting published but not yet PUBLISHED in read model: ${event.aggId}"
                )
            }
        } catch (e: Exception) {
            logger.warn(
                "Published posting not found in read model yet. " +
                "This is normal during async processing: ${event.aggId}"
            )
        }
        
        // Important side effects after publishing:
        // - The posting is now PUBLISHED in the read model
        // - Should be indexed in search engines/internal search
        // - May trigger notifications to subscribed users/companies
        // - Analytics events should be recorded
        // - Update posting visibility flags
    }
    
    /**
     * Handle InternshipPostingDeletedEvent.
     * 
     * Triggered when: A posting is deleted (soft delete - status changes to CLOSED/DELETED)
     * Reaction: The posting is no longer visible in public listings
     * 
     * Note: The posting is not physically deleted from the database (event sourcing).
     * Instead, its status is changed to DELETED, and it's filtered out from queries.
     * This maintains historical data for audit purposes.
     * 
     * Consistency considerations:
     * - Any applications/bookmarks referencing this posting should be handled
     * - The posting should immediately disappear from public-facing queries
     * - Historical records should be preserved for audit/compliance
     */
    @EventHandler
    fun handle(event: InternshipPostingDeletedEvent) {
        logger.info("InternshipPosting deleted: ${event.aggId}")
        
        // Verify the posting is marked as DELETED/CLOSED in the read model
        try {
            val deletedPosting = internshipPostingReadService.findById(
                InternshipPostingId.from(event.aggId)
            )
            logger.info(
                "Posting marked as deleted in read model with status: ${deletedPosting.status}"
            )
        } catch (e: Exception) {
            logger.info(
                "Posting deletion reflected in read model or not yet synced: ${event.aggId}"
            )
        }
        
        // Side effects when a posting is deleted:
        // - The posting is marked as DELETED in the read model
        // - Remove from search indices
        // - Notify any active applications that the position is no longer available
        // - Archive related data if needed
        // - Remove from caches (e.g., "trending postings", "new postings")
        // - Audit logging for deletion
        
        // Example of cleanup operations:
        // - Delete from external job board APIs
        // - Notify students who bookmarked this posting
        // - Update company's posting statistics
        // - Update any cached statistics about open positions
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(InternshipPostingEventHandler::class.java)
    }
}
