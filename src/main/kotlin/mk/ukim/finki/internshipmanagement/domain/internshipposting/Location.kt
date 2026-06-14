package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable



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