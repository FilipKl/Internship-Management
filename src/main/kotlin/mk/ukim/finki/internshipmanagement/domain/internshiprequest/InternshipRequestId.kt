package mk.ukim.finki.internshipmanagement.domain.internshiprequest

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.core.JsonParser
import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.UUID

class InternshipRequestIdDeserializer : JsonDeserializer<InternshipRequestId>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): InternshipRequestId {
        return InternshipRequestId.from(p.text)
    }
}

@Embeddable
@JsonDeserialize(using = InternshipRequestIdDeserializer::class)
data class InternshipRequestId(val id: UUID = UUID.randomUUID()) : Identifier<UUID> {

    companion object {
        private const val PREFIX = "InternshipRequest:"

        fun generate(): InternshipRequestId {
            return InternshipRequestId(UUID.randomUUID())
        }

        fun from(uuidString: String): InternshipRequestId {
            val cleaned = uuidString.removePrefix(PREFIX)
            return InternshipRequestId(UUID.fromString(cleaned))
        }

        fun from(uuid: UUID): InternshipRequestId {
            return InternshipRequestId(uuid)
        }
    }

    override fun getValue(): UUID = id

    @JsonValue
    override fun toString(): String = id.toString()
}