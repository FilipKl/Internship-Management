package mk.ukim.finki.internshipmanagement.domain.internshipposting

import jakarta.persistence.Embeddable


@Embeddable
data class Salary(
    val amount: Double? = null,
    val currency: String = "USD"
) {
    init {
        if (amount != null) {
            require(amount >= 0) { "Salary amount cannot be negative" }
        }
        require(currency.isNotBlank()) { "Currency cannot be blank" }
        require(currency.length == 3) { "Currency must be a 3-letter code (e.g., USD, EUR)" }
    }
    
    companion object {
        fun unpaid(): Salary = Salary(amount = null, currency = "USD")
        fun of(amount: Double, currency: String = "USD"): Salary = Salary(amount, currency)
    }
    
    override fun toString(): String = if (amount != null) "$amount $currency" else "Unpaid"
}

