package mk.ukim.finki.internshipmanagement.presentation.kafka.documentation

import io.github.springwolf.core.asyncapi.annotations.AsyncListener
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCompletedExternalEvent
import mk.ukim.finki.internshipmanagement.domain.InternshipJournal.events.InternshipJournalCreatedExternalEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingCreatedExternalEvent
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.InternshipPostingPublishedExternalEvent
import mk.ukim.finki.internshipmanagement.domain.internshiprequest.events.InternshipRequestApprovedExternalEvent
import org.springframework.stereotype.Component

/**
 * Documents all external Kafka events published by the Internship Management service.
 *
 * This class is purely for documentation via Springwolf/AsyncAPI.
 * The methods are never called at runtime — they exist only so Springwolf can extract
 * the event topic names and payload types.
 *
 * Actual event publishing happens through EventMessagingEventHandler in the infrastructure layer.
 */
@Component
class KafkaEventDocumentation {

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "internship.posting.created",
            description = "Published when a new internship posting is created in the system."
        )
    )
    fun publishInternshipPostingCreated(payload: InternshipPostingCreatedExternalEvent) {}

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "internship.posting.published",
            description = "Published when an internship posting is published and becomes visible to students."
        )
    )
    fun publishInternshipPostingPublished(payload: InternshipPostingPublishedExternalEvent) {}

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "internship.journal.created",
            description = "Published when a new internship journal is created for a student."
        )
    )
    fun publishInternshipJournalCreated(payload: InternshipJournalCreatedExternalEvent) {}

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "internship.journal.completed",
            description = "Published when an internship journal is marked as completed."
        )
    )
    fun publishInternshipJournalCompleted(payload: InternshipJournalCompletedExternalEvent) {}

    @AsyncListener(
        operation = AsyncOperation(
            channelName = "internship.request.approved",
            description = "Published when an internship request is approved by a coordinator."
        )
    )
    fun publishInternshipRequestApproved(payload: InternshipRequestApprovedExternalEvent) {}
}



