package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import jakarta.persistence.Embeddable
import java.util.*

/**
 * Strongly-typed identifier for InternshipJournal aggregate root.
 * Includes a human-readable prefix for easy identification in logs and databases.
 */
@Embeddable
data class InternshipJournalId(
    val id: String = "" // No-arg constructor for JPA / Axon
) : Identifier<String> {

    // Convenience constructor for new IDs
    constructor(uuid: UUID) : this("InternshipJournal:$uuid")

    companion object {
        private const val PREFIX = "InternshipJournal:"

        fun generate(): InternshipJournalId = InternshipJournalId(UUID.randomUUID())

        fun from(idString: String): InternshipJournalId {
            return if (idString.startsWith(PREFIX)) {
                InternshipJournalId(idString)
            } else {
                InternshipJournalId("$PREFIX$idString")
            }
        }
    }

    override fun getValue(): String = id
    override fun toString(): String = id
}
