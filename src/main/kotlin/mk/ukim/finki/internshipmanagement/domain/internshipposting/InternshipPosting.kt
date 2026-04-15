package mk.ukim.finki.internshipmanagement.domain.internshipposting

import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDateTime
import mk.ukim.finki.internshipmanagement.domain.internshipposting.JobTitle
import mk.ukim.finki.internshipmanagement.domain.internshipposting.CompanyName
import mk.ukim.finki.internshipmanagement.domain.internshipposting.Description
import mk.ukim.finki.internshipmanagement.domain.internshipposting.TechStack
import mk.ukim.finki.internshipmanagement.domain.internshipposting.Location

/**
 * InternshipPosting Aggregate Root
 * Manages internship posting lifecycle: Create, Update, Edit, Publish, Delete
 *
 * Uses Axon event sourcing - state is reconstructed from events, not persisted directly.
 *
 * Uses value objects for type-safe domain modeling:
 * - InternshipPostingId: Strongly-typed identifier
 * - Title, Description, Company, TechStack, Location: Embedded validated value objects
 */
@Aggregate
class InternshipPosting {

    @AggregateIdentifier
    lateinit var internshipPostingId: InternshipPostingId

    lateinit var title: JobTitle

    lateinit var company: CompanyName

    lateinit var description: Description

    lateinit var techStack: TechStack

    lateinit var location: Location

    lateinit var status: PostingStatus

    lateinit var createdAt: LocalDateTime

    var updatedAt: LocalDateTime? = null

    constructor()

    // ==================== Command Handlers ====================

    /**
     * Command Handler: Create a new InternshipPosting.
     */
    @CommandHandler
    constructor(command: CreateInternshipPostingCommand) {
        AggregateLifecycle.apply(InternshipPostingCreatedEvent(command))
    }

    @CommandHandler
    fun handle(command: UpdateInternshipPostingCommand) {
        check(status.canUpdate()) { "Cannot update internship posting in $status status" }
        AggregateLifecycle.apply(InternshipPostingUpdatedEvent(command))
    }

    @CommandHandler
    fun handle(command: EditInternshipPostingCommand) {
        check(status.canUpdate()) { "Cannot edit internship posting in $status status" }
        AggregateLifecycle.apply(InternshipPostingEditedEvent(command))
    }

    @CommandHandler
    fun handle(command: PublishInternshipPostingCommand) {
        check(status.canPublish()) { "Cannot publish internship posting in $status status" }
        AggregateLifecycle.apply(InternshipPostingPublishedEvent(command))
    }

    @CommandHandler
    fun handle(command: DeleteInternshipPostingCommand) {
        check(status.canClose()) { "Cannot delete internship posting in $status status" }
        AggregateLifecycle.apply(InternshipPostingDeletedEvent(command))
    }

    // ==================== Event Handlers ====================

    @EventSourcingHandler
    fun on(event: InternshipPostingCreatedEvent) {
        internshipPostingId = event.internshipPostingId
        title = JobTitle(event.title)
        company = CompanyName(event.company)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        // Parse location string - format: "city, country" or "city, country (Remote)"
        val isRemote = event.location.contains("(Remote)")
        val locationParts = event.location.replace(" (Remote)", "").split(",").map { it.trim() }
        val city = if (locationParts.isNotEmpty()) locationParts[0] else "Unknown"
        val country = if (locationParts.size > 1) locationParts[1] else "Unknown"
        location = Location(city, country, isRemote)
        status = PostingStatus.DRAFT
        createdAt = LocalDateTime.now()
    }

    @EventSourcingHandler
    fun on(event: InternshipPostingUpdatedEvent) {
        title = JobTitle(event.title)
        company = CompanyName(event.company)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        // Parse location string
        val isRemote = event.location.contains("(Remote)")
        val locationParts = event.location.replace(" (Remote)", "").split(",").map { it.trim() }
        val city = if (locationParts.isNotEmpty()) locationParts[0] else "Unknown"
        val country = if (locationParts.size > 1) locationParts[1] else "Unknown"
        location = Location(city, country, isRemote)
        updatedAt = LocalDateTime.now()
    }

    @EventSourcingHandler
    fun on(event: InternshipPostingEditedEvent) {
        title = JobTitle(event.title)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        updatedAt = LocalDateTime.now()
    }

    @EventSourcingHandler
    fun on(event: InternshipPostingPublishedEvent) {
        status = PostingStatus.PUBLISHED
        updatedAt = LocalDateTime.now()
    }

    @EventSourcingHandler
    fun on(event: InternshipPostingDeletedEvent) {
        status = PostingStatus.CLOSED
        updatedAt = LocalDateTime.now()
    }

    // ==================== Status Enum ====================

    enum class PostingStatus {
        DRAFT, PUBLISHED, CLOSED;

        fun canUpdate() = this == DRAFT || this == PUBLISHED
        fun canPublish() = this == DRAFT
        fun canClose() = this == PUBLISHED
    }
}