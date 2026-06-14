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

@Component
class InternshipPostingEventHandler(
    private val internshipPostingReadService: InternshipPostingViewReadService,
    private val commandGateway: CommandGateway
) {

    @EventHandler
    fun handle(event: InternshipPostingCreatedEvent) {
        logger.info(
            "InternshipPosting created: ${event.internshipPostingId} | " +
            "Title: ${event.title} | Company: ${event.company}"
        )

        try {
            val createdPosting = internshipPostingReadService.findById(
                event.internshipPostingId
            )
            logger.info(
                "InternshipPostingView successfully created and is available for queries: " +
                "${createdPosting.jobTitle} at ${createdPosting.companyName}"
            )
        } catch (e: Exception) {
            logger.warn(
                "InternshipPostingView not yet available in read model. " +
                "This is normal during async event processing: ${event.internshipPostingId}"
            )
        }
    }

    @EventHandler
    fun handle(event: InternshipPostingUpdatedEvent) {
        logger.info(
            "InternshipPosting updated: ${event.internshipPostingId} | " +
            "Title: ${event.title} | Tech Stack: ${event.techStack}"
        )
        try {
            val updatedPosting = internshipPostingReadService.findById(
                event.internshipPostingId
            )
            logger.info(
                "Updated posting verified in read model: ${updatedPosting.jobTitle} " +
                "(Status: ${updatedPosting.status})"
            )
        } catch (e: Exception) {
            logger.warn(
                "Updated posting not yet available in read model. " +
                "Updates may not be immediately visible: ${event.internshipPostingId}"
            )
        }
    }

    @EventHandler
    fun handle(event: InternshipPostingEditedEvent) {
        logger.info(
            "InternshipPosting edited: ${event.internshipPostingId} | " +
            "New Title: ${event.title}"
        )
    }

    @EventHandler
    fun handle(event: InternshipPostingPublishedEvent) {
        logger.info(
            "InternshipPosting published: ${event.internshipPostingId} | " +
            "Published by: ${event.publishedBy}"
        )
        

        try {
            val publishedPosting = internshipPostingReadService.findById(
                event.internshipPostingId
            )
            
            if (publishedPosting.isPublished()) {
                logger.info(
                    "Posting successfully published and visible in read model: " +
                    "${publishedPosting.jobTitle} for ${publishedPosting.companyName}"
                )

            } else {
                logger.warn(
                    "Posting published but not yet PUBLISHED in read model: ${event.internshipPostingId}"
                )
            }
        } catch (e: Exception) {
            logger.warn(
                "Published posting not found in read model yet. " +
                "This is normal during async processing: ${event.internshipPostingId}"
            )
        }
    }

    @EventHandler
    fun handle(event: InternshipPostingDeletedEvent) {
        logger.info("InternshipPosting deleted: ${event.internshipPostingId}")

        try {
            val deletedPosting = internshipPostingReadService.findById(
                event.internshipPostingId
            )
            logger.info(
                "Posting marked as deleted in read model with status: ${deletedPosting.status}"
            )
        } catch (e: Exception) {
            logger.info(
                "Posting deletion reflected in read model or not yet synced: ${event.internshipPostingId}"
            )
        }
    }
    
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(InternshipPostingEventHandler::class.java)
    }
}
