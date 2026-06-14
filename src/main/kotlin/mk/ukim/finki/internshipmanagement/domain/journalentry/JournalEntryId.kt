package mk.ukim.finki.internshipmanagement.domain.journalentry

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.*


class JournalEntryIdDeserializer : JsonDeserializer<JournalEntryId>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): JournalEntryId {
        return JournalEntryId.from(p.text)
    }
}


@Embeddable
@JsonDeserialize(using = JournalEntryIdDeserializer::class)
data class JournalEntryId(val id: UUID = UUID.randomUUID()) : Identifier<UUID> {

    companion object {
        private const val PREFIX = "JournalEntry:"

        fun generate(): JournalEntryId {
            return JournalEntryId(UUID.randomUUID())
        }

        fun from(uuidString: String): JournalEntryId {
            val cleaned = uuidString.removePrefix(PREFIX)
            return JournalEntryId(UUID.fromString(cleaned))
        }


        fun from(uuid: UUID): JournalEntryId {
            return JournalEntryId(uuid)
        }
    }

    override fun getValue(): UUID = id

    @JsonValue
    override fun toString(): String = id.toString()
}

