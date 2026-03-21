package mk.ukim.finki.internshipmanagement.domain.internshipposting.commands

import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import java.time.LocalDate

data class CreateInternshipPostingCommand(
    val id: InternshipPostingId,
    val title: String,
    val company: String,
    val description: String,
    val postedDate: LocalDate,
    val validUntil: LocalDate,
    val location: String,
    val techStack: String,
    val contactEmail: String
)

data class UpdateInternshipPostingCommand(
    val id: InternshipPostingId,
    val title: String,
    val company: String,
    val description: String,
    val validUntil: LocalDate,
    val location: String,
    val techStack: String,
    val contactEmail: String
)

data class EditInternshipPostingCommand(
    val id: InternshipPostingId,
    val title: String,
    val description: String,
    val techStack: String
)

data class PublishInternshipPostingCommand(
    val id: InternshipPostingId,
    val publishedBy: String
)

data class DeleteInternshipPostingCommand(
    val id: InternshipPostingId
)