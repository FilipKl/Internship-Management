package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.*

/**
 * Strongly-typed identifier for InternshipPosting aggregate.
 * Wraps UUID in a value object to prevent mixing IDs from different aggregates.
 * Example: "InternshipPosting:550e8400-e29b-41d4-a716-446655440000"
 */
@Embeddable
data class InternshipPostingId(val id: String = "") : Identifier<String> {

    companion object {
        private const val PREFIX = "InternshipPosting:"

        /**
         * Create a new InternshipPostingId with a random UUID.
         */
        fun generate(): InternshipPostingId {
            return InternshipPostingId(PREFIX + UUID.randomUUID())
        }

        /**
         * Create from existing UUID string (adds prefix if not present).
         */
        fun from(uuidString: String): InternshipPostingId {
            return if (uuidString.startsWith(PREFIX)) {
                InternshipPostingId(uuidString)
            } else {
                InternshipPostingId(PREFIX + uuidString)
            }
        }

        /**
         * Create from UUID object.
         */
        fun from(uuid: UUID): InternshipPostingId {
            return InternshipPostingId(PREFIX + uuid.toString())
        }
    }

    /**
     * Get the raw UUID part (without prefix).
     */
    fun getRawId(): String = id.replace(PREFIX, "")

    override fun getValue(): String = id

    override fun toString(): String = id
}