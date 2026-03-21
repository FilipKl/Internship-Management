package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.AggregateRoot
import mk.ukim.finki.internshipmanagement.domain.internshipposting.commands.*
import mk.ukim.finki.internshipmanagement.domain.internshipposting.events.*
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
 * Uses value objects for type-safe domain modeling:
 * - InternshipPostingId: Strongly-typed identifier
 * - Title, Description, Company, TechStack, Location: Embedded validated value objects
 */
@Entity
@Table(name = "internship_postings")
class InternshipPosting : AggregateRoot {

    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    lateinit var internshipPostingId: InternshipPostingId

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "title"))
    lateinit var title: JobTitle

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "company"))
    lateinit var company: CompanyName

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "description", columnDefinition = "TEXT"))
    lateinit var description: Description

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "tech_stack"))
    lateinit var techStack: TechStack

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "location"))
    lateinit var location: Location

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var status: PostingStatus

    @Column(nullable = false)
    lateinit var createdAt: LocalDateTime

    var updatedAt: LocalDateTime? = null

    constructor()

    // ==================== Command Handlers ====================

    constructor(command: CreateInternshipPostingCommand) {
        internshipPostingId = InternshipPostingId.generate()
        status = PostingStatus.DRAFT
        recordEvent(InternshipPostingCreatedEvent(command))
    }

    fun handle(command: UpdateInternshipPostingCommand) {
        check(status.canUpdate()) { "Cannot update internship posting in $status status" }
        recordEvent(InternshipPostingUpdatedEvent(command))
    }

    fun handle(command: EditInternshipPostingCommand) {
        check(status.canUpdate()) { "Cannot edit internship posting in $status status" }
        recordEvent(InternshipPostingEditedEvent(command))
    }

    fun handle(command: PublishInternshipPostingCommand) {
        check(status.canPublish()) { "Cannot publish internship posting in $status status" }
        recordEvent(InternshipPostingPublishedEvent(command))
    }

    fun handle(command: DeleteInternshipPostingCommand) {
        check(status.canClose()) { "Cannot delete internship posting in $status status" }
        recordEvent(InternshipPostingDeletedEvent(command))
    }

    // ==================== Event Handlers ====================

    fun on(event: InternshipPostingCreatedEvent) {
        internshipPostingId = InternshipPostingId.from(event.aggregateId)
        title = JobTitle(event.title)
        company = CompanyName(event.company)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        location = Location(event.location)
        status = PostingStatus.DRAFT
        createdAt = LocalDateTime.now()
    }

    fun on(event: InternshipPostingUpdatedEvent) {
        title = JobTitle(event.title)
        company = CompanyName(event.company)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        location = Location(event.location)
        updatedAt = LocalDateTime.now()
    }

    fun on(event: InternshipPostingEditedEvent) {
        title = JobTitle(event.title)
        description = Description(event.description)
        techStack = TechStack(event.techStack)
        updatedAt = LocalDateTime.now()
    }

    fun on(event: InternshipPostingPublishedEvent) {
        status = PostingStatus.PUBLISHED
        updatedAt = LocalDateTime.now()
    }

    fun on(event: InternshipPostingDeletedEvent) {
        status = PostingStatus.CLOSED
        updatedAt = LocalDateTime.now()
    }




    enum class PostingStatus {
        DRAFT, PUBLISHED, CLOSED;

        fun canUpdate() = this == DRAFT || this == PUBLISHED
        fun canPublish() = this == DRAFT
        fun canClose() = this == PUBLISHED
    }
}