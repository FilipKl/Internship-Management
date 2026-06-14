package mk.ukim.finki.internshipmanagement.application.internshipposting.exception

class InternshipPostingNotFoundException(
    val postingId: String,
    message: String = "Internship posting with ID '$postingId' not found"
) : RuntimeException(message)

