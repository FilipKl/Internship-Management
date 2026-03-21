package mk.ukim.finki.internshipmanagement.application.internshipposting.exception

/**
 * Exception thrown when an internship posting is not found.
 * Typically used in query and retrieval operations.
 */
class InternshipPostingNotFoundException(
    val postingId: String,
    message: String = "Internship posting with ID '$postingId' not found"
) : RuntimeException(message)

