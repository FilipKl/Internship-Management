package mk.ukim.finki.internshipmanagement.domain.InternshipJournal

import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import jakarta.persistence.Embeddable
import java.util.*


@Embeddable
data class InternshipJournalId(
    val id: String = ""
) : Identifier<String> {

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
