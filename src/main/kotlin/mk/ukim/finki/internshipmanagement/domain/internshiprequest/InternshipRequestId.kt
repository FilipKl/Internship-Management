package mk.ukim.finki.internshipmanagement.domain.internshiprequest
import jakarta.persistence.Embeddable
import mk.ukim.finki.internshipmanagement.domain.common.Identifier
import java.util.UUID
@Embeddable
data class InternshipRequestId(val value: String = "") : Identifier<String> {
    constructor(uuid: UUID) : this("InternshipRequest:$uuid")
    override fun getValue(): String = value
    override fun toString(): String = value
}