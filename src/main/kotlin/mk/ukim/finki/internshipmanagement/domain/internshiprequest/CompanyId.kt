package mk.ukim.finki.internshipmanagement.domain.internshiprequest

import jakarta.persistence.Embeddable

@Embeddable
data class CompanyId(val value: String = "") {
    override fun toString(): String = value
}

