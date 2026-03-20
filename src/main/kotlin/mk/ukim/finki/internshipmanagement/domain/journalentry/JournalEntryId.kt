package mk.ukim.finki.internshipmanagement.domain.journalentry

import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.*

/**
 * Strongly-typed identifier for JournalEntry aggregate.
 * Wraps UUID in a value object to prevent mixing IDs from different aggregates.
 * Example: "JournalEntry:550e8400-e29b-41d4-a716-446655440000"
 */
@Embeddable
data class JournalEntryId(val id: String = "") : Identifier<String> {
    
    companion object {
        private const val PREFIX = "JournalEntry:"
        
        /**
         * Create a new JournalEntryId with a random UUID.
         */
        fun generate(): JournalEntryId {
            return JournalEntryId(PREFIX + UUID.randomUUID())
        }
        
        /**
         * Create from existing UUID string (adds prefix if not present).
         */
        fun from(uuidString: String): JournalEntryId {
            return if (uuidString.startsWith(PREFIX)) {
                JournalEntryId(uuidString)
            } else {
                JournalEntryId(PREFIX + uuidString)
            }
        }
        
        /**
         * Create from UUID object.
         */
        fun from(uuid: UUID): JournalEntryId {
            return JournalEntryId(PREFIX + uuid.toString())
        }
    }
    
    /**
     * Get the raw UUID part (without prefix).
     */
    fun getRawId(): String = id.replace(PREFIX, "")
    
    override fun getValue(): String = id
    
    override fun toString(): String = id
}

