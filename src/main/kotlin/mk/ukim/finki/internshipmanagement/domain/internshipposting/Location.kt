package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable

/**
 * Value object representing the location for an internship posting.
 *
 * Constraints:
 * - City and country must be non-blank
 * - City and country must not exceed 100 characters each
 * - Can be marked as remote
 */

@Embeddable
data class Location(
    val city: String = "",
    val country: String = "",
    val isRemote: Boolean = false
) {
    init {
        require(city.isNotBlank()) { "City cannot be blank" }
        require(country.isNotBlank()) { "Country cannot be blank" }
    }

    override fun toString(): String = if (isRemote) "$city, $country (Remote)" else "$city, $country"
}