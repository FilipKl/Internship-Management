package mk.ukim.finki.internshipmanagement.domain.internshipposting

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.*

/**
 * Custom deserializer for InternshipPostingId
 */
class InternshipPostingIdDeserializer : JsonDeserializer<InternshipPostingId>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): InternshipPostingId {
        return InternshipPostingId.from(p.text)
    }
}

/**
 * Strongly-typed identifier for InternshipPosting aggregate.
 * Wraps UUID in a value object to prevent mixing IDs from different aggregates.
 */
@Embeddable
@JsonDeserialize(using = InternshipPostingIdDeserializer::class)
data class InternshipPostingId(val id: UUID = UUID.randomUUID()) : Identifier<UUID> {

    companion object {
        private const val PREFIX = "InternshipPosting:"

        /**
         * Create a new InternshipPostingId with a random UUID.
         */
        fun generate(): InternshipPostingId {
            return InternshipPostingId(UUID.randomUUID())
        }

        /**
         * Create from existing UUID string.
         */
        fun from(uuidString: String): InternshipPostingId {
            val cleaned = uuidString.removePrefix(PREFIX)
            return InternshipPostingId(UUID.fromString(cleaned))
        }

        /**
         * Create from UUID object.
         */
        fun from(uuid: UUID): InternshipPostingId {
            return InternshipPostingId(uuid)
        }
    }

    override fun getValue(): UUID = id

    @JsonValue
    override fun toString(): String = id.toString()
}