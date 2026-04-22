package mk.ukim.finki.internshipmanagement.presentation.internshipposting.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import mk.ukim.finki.internshipmanagement.application.internshipposting.query.InternshipPostingViewReadService
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPosting
import mk.ukim.finki.internshipmanagement.application.internshipposting.query.InternshipPostingView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for InternshipPosting read operations.
 * Provides HTTP endpoints for querying the InternshipPosting read model.
 */
@RestController
@RequestMapping("/api/v1/internship-postings")
@Tag(
    name = "Internship Posting Query API",
    description = "API for querying internship posting data. Browse available postings by various filters such as status, company, location, and technology stack."
)
class InternshipPostingController(
    private val readService: InternshipPostingViewReadService
) {
    
    @Operation(
        summary = "Get all internship postings",
        description = "Retrieves a list of all internship postings in the system."
    )
    @GetMapping("/all")
    fun findAll(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findAll())
    
    @Operation(
        summary = "Get internship posting by ID",
        description = "Retrieves a specific internship posting by its unique identifier."
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<InternshipPostingView> =
        ResponseEntity.ok(readService.findById(InternshipPostingId.from(id)))
    
    @Operation(
        summary = "Find postings by status",
        description = "Retrieves all internship postings filtered by their status (DRAFT, PUBLISHED, ARCHIVED, CLOSED)."
    )
    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByStatus(InternshipPosting.PostingStatus.valueOf(status.uppercase())))
    
    @Operation(
        summary = "Find postings by company",
        description = "Retrieves all internship postings offered by a specific company."
    )
    @GetMapping("/company/{company}")
    fun findByCompany(@PathVariable company: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByCompany(company))
    
    @Operation(
        summary = "Find postings by title keyword",
        description = "Searches internship postings by title keyword using case-insensitive matching."
    )
    @GetMapping("/title/{keyword}")
    fun findByTitleContaining(@PathVariable keyword: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByTitleContaining(keyword))
    
    @Operation(
        summary = "Find postings by technology stack",
        description = "Retrieves internship postings that require or use specific technologies."
    )
    @GetMapping("/tech-stack/{techStack}")
    fun findByTechStack(@PathVariable techStack: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByTechStack(techStack))
    
    @Operation(
        summary = "Find postings by location",
        description = "Retrieves internship postings available in a specific location."
    )
    @GetMapping("/location/{location}")
    fun findByLocation(@PathVariable location: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByLocation(location))
    
    @Operation(
        summary = "Get all published postings",
        description = "Retrieves all internship postings that are currently published and visible to students."
    )
    @GetMapping("/published")
    fun findAllPublished(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.getAllPublishedPostings())
    
    @Operation(
        summary = "Get remote internship postings",
        description = "Retrieves all internship postings that offer remote work opportunities."
    )
    @GetMapping("/remote")
    fun findRemote(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.getRemotePostings())
    
    @Operation(
        summary = "Find postings by status and company",
        description = "Retrieves internship postings filtered by both status and company name."
    )
    @GetMapping("/status/{status}/company/{company}")
    fun findByStatusAndCompany(
        @PathVariable status: String,
        @PathVariable company: String
    ): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByStatusAndCompany(
            InternshipPosting.PostingStatus.valueOf(status.uppercase()),
            company
        ))
    
    @Operation(
        summary = "Count postings by status",
        description = "Returns the total count of internship postings with a specific status."
    )
    @GetMapping("/count/{status}")
    fun countByStatus(@PathVariable status: String): ResponseEntity<Long> =
        ResponseEntity.ok(readService.countByStatus(InternshipPosting.PostingStatus.valueOf(status.uppercase())))

}

