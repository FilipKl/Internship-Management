package mk.ukim.finki.internshipmanagement.application.journalentry.query

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import mk.ukim.finki.internshipmanagement.domain.common.LabeledEntity
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntry
import mk.ukim.finki.internshipmanagement.domain.journalentry.JournalEntryId
import org.hibernate.annotations.Immutable
import java.time.LocalDateTime

/**
 * Read model / View for JournalEntry aggregate.
 * Immutable projection optimized for display/query purposes.
 */
@Entity
@Table(name = "journal_entries")
@Immutable
data class JournalEntryView(
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val journalEntryId: JournalEntryId = JournalEntryId(),

    @Column(nullable = false)
    val journalId: String = "",

    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = false)
    val content: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: JournalEntry.EntryStatus = JournalEntry.EntryStatus.DRAFT,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val editedAt: LocalDateTime? = null,
    val reviewedAt: LocalDateTime? = null,
    val reviewedBy: String? = null
) : LabeledEntity {

    fun getContentPreview(): String = if (content.length > 100) "${content.substring(0, 100)}..." else content

    fun isReviewed(): Boolean = status != JournalEntry.EntryStatus.DRAFT

    override fun getId(): Identifier<*> = journalEntryId

    override fun getLabel(): String = "$title - ${status.name} (${createdAt.toLocalDate()})"
}