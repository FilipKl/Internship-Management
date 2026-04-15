package mk.ukim.finki.internshipmanagement.presentation.internshipposting.controller

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
class InternshipPostingController(
    private val readService: InternshipPostingViewReadService
) {
    
    @GetMapping("/all")
    fun findAll(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findAll())
    
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<InternshipPostingView> =
        ResponseEntity.ok(readService.findById(InternshipPostingId.from(id)))
    
    @GetMapping("/by-status/{status}")
    fun findByStatus(@PathVariable status: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByStatus(InternshipPosting.PostingStatus.valueOf(status.uppercase())))
    
    @GetMapping("/company/{company}")
    fun findByCompany(@PathVariable company: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByCompany(company))
    
    @GetMapping("/title/{keyword}")
    fun findByTitleContaining(@PathVariable keyword: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByTitleContaining(keyword))
    
    @GetMapping("/tech-stack/{techStack}")
    fun findByTechStack(@PathVariable techStack: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByTechStack(techStack))
    
    @GetMapping("/location/{location}")
    fun findByLocation(@PathVariable location: String): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByLocation(location))
    
    @GetMapping("/published")
    fun findAllPublished(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.getAllPublishedPostings())
    
    @GetMapping("/remote")
    fun findRemote(): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.getRemotePostings())
    
    @GetMapping("/status/{status}/company/{company}")
    fun findByStatusAndCompany(
        @PathVariable status: String,
        @PathVariable company: String
    ): ResponseEntity<List<InternshipPostingView>> =
        ResponseEntity.ok(readService.findByStatusAndCompany(
            InternshipPosting.PostingStatus.valueOf(status.uppercase()),
            company
        ))
    
    @GetMapping("/count/{status}")
    fun countByStatus(@PathVariable status: String): ResponseEntity<Long> =
        ResponseEntity.ok(readService.countByStatus(InternshipPosting.PostingStatus.valueOf(status.uppercase())))

}

