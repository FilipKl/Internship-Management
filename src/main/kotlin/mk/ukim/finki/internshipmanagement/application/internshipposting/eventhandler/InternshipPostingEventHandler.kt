package mk.ukim.finki.internshipmanagement.application.internshipposting.eventhandler

import mk.ukim.finki.internshipmanagement.application.internshipposting.query.InternshipPostingViewReadService
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingCreatedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingUpdatedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingEditedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingPublishedEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingDeletedEvent
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
    private val internshipPostingReadService: InternshipPostingViewReadService
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
        // event projections. This log serves as a tracking/audit point.
        // Additional side effects can be performed here if needed.
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
        
        // The read model projection is updated through event handlers in the
        // InternshipPostingViewRepository or through a dedicated projection handler.
        // This handler serves as a coordination point for any additional logic needed
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
        
        // Important side effects after publishing:
        // - The posting is now PUBLISHED in the read model
        // - Should be indexed in search engines/internal search
        // - May trigger notifications to subscribed users/companies
        // - Analytics events should be recorded
        // - Update posting visibility flags
        
        // Example of additional operations (if needed in your business logic):
        // - Send email notifications to interested students
        // - Update a cache of "active postings"
        // - Trigger an external API call to a job board aggregator
        // - Log to an audit trail system
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
