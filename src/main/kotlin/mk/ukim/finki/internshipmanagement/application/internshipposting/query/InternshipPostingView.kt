package mk.ukim.finki.internshipmanagement.application.internshipposting.query

import jakarta.persistence.*
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

/**
 * Embedded Location value object for InternshipPostingView.
 */
@Embeddable
data class Location(
    @Column(nullable = false)
    val country: String = "",

    @Column(nullable = false)
    val city: String = "",

    @Column(nullable = false)
    val isRemote: Boolean = false
)

/**
 * Read model / View for InternshipPosting aggregate.
 * Immutable projection optimized for display/query purposes.
 */
@Entity
@Table(name = "internship_postings")
@Immutable
data class InternshipPostingView(
    @EmbeddedId
    @AttributeOverride(name = "id", column = Column(name = "id"))
    val internshipPostingId: InternshipPostingId = InternshipPostingId(),

    @Column(nullable = false)
    val jobTitle: String = "",

    @Column(nullable = false)
    val companyName: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String = "",

    @Column(name = "tech_stack", nullable = false)
    val techStack: String = "",

    @Embedded
    val location: Location = Location(),

    @Column(nullable = false)
    val status: String = "DRAFT",

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime? = null
) : LabeledEntity {

    fun getDescriptionPreview(): String = if (description.length > 100) "${description.substring(0, 100)}..." else description

    fun isPublished(): Boolean = status == "PUBLISHED"

    override fun getId(): Identifier<*> = internshipPostingId

    override fun getLabel(): String = "$jobTitle - $status (${createdAt.toLocalDate()})"
}