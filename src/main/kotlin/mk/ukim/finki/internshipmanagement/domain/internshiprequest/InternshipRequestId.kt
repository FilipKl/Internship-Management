package mk.ukim.finki.internshipmanagement.domain.internshiprequest
import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.UUID
@Embeddable
data class InternshipRequestId(private val raw: String = "") : Identifier<String> {
    constructor(uuid: UUID) : this("InternshipRequest:$uuid")
    override fun getValue(): String = raw
    override fun toString(): String = raw
}