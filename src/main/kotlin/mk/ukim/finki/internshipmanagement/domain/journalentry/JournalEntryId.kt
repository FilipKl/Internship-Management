package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.*

/**
 * Strongly-typed identifier for JournalEntry aggregate.
 * Wraps UUID in a value object to prevent mixing IDs from different aggregates.
 */
@Embeddable
data class JournalEntryId(val id: UUID = UUID.randomUUID()) : Identifier<UUID> {

    companion object {
        private const val PREFIX = "JournalEntry:"

        /**
         * Create a new JournalEntryId with a random UUID.
         */
        fun generate(): JournalEntryId {
            return JournalEntryId(UUID.randomUUID())
        }

        /**
         * Create from existing UUID string.
         */
        fun from(uuidString: String): JournalEntryId {
            val cleaned = uuidString.removePrefix(PREFIX)
            return JournalEntryId(UUID.fromString(cleaned))
        }

        /**
         * Create from UUID object.
         */
        fun from(uuid: UUID): JournalEntryId {
            return JournalEntryId(uuid)
        }
    }

    override fun getValue(): UUID = id

    override fun toString(): String = "$PREFIX$id"
}

