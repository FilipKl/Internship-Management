package mk.ukim.finki.internshipmanagement.application.internshipposting.query

import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPosting
import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Read service for InternshipPosting aggregate.
 * Provides query methods for the read model.
 */
interface InternshipPostingViewReadService {

    fun findAll(): List<InternshipPostingView>

    fun findById(id: InternshipPostingId): InternshipPostingView

    fun findByStatus(status: InternshipPosting.PostingStatus): List<InternshipPostingView>

    fun findByCompany(company: String): List<InternshipPostingView>

    fun findByTitleContaining(keyword: String): List<InternshipPostingView>

    fun findByStatusAndCompany(status: InternshipPosting.PostingStatus, company: String): List<InternshipPostingView>

    fun findByTechStack(techStack: String): List<InternshipPostingView>

    fun findByLocation(location: String): List<InternshipPostingView>

    fun countByStatus(status: InternshipPosting.PostingStatus): Long

    fun getAllPublishedPostings(): List<InternshipPostingView>

    /**
     * Get all internship postings (including draft and closed).
     */
    fun getAllPostings(): List<InternshipPostingView>

    fun getRemotePostings(): List<InternshipPostingView>

}

@Service
@Transactional(readOnly = true)
class InternshipPostingViewReadServiceImpl(
    private val viewRepository: InternshipPostingViewRepository
) : InternshipPostingViewReadService {

    override fun findAll(): List<InternshipPostingView> = viewRepository.findAll()

    override fun findById(id: InternshipPostingId): InternshipPostingView =
        viewRepository.findById(id).orElseThrow { RuntimeException("Posting not found: $id") }

    override fun findByStatus(status: InternshipPosting.PostingStatus): List<InternshipPostingView> =
        viewRepository.findByStatus(status.name)

    override fun findByCompany(company: String): List<InternshipPostingView> =
        viewRepository.findByCompanyNameContainingIgnoreCase(company)

    override fun findByTitleContaining(keyword: String): List<InternshipPostingView> =
        viewRepository.findByJobTitleContainingIgnoreCase(keyword)

    override fun findByStatusAndCompany(
        status: InternshipPosting.PostingStatus,
        company: String
    ): List<InternshipPostingView> =
        viewRepository.findByStatusAndCompanyNameContainingIgnoreCase(status.name, company)

    override fun findByTechStack(techStack: String): List<InternshipPostingView> =
        viewRepository.findByTechStackContainingIgnoreCase(techStack)

    override fun findByLocation(location: String): List<InternshipPostingView> {
        val byCity = viewRepository.findByLocationCityContainingIgnoreCase(location)
        val byCountry = viewRepository.findByLocationCountryContainingIgnoreCase(location)
        val remote = if (location.equals("remote", ignoreCase = true)) viewRepository.findByLocationIsRemoteTrue() else emptyList()
        return (byCity + byCountry + remote).distinctBy { it.internshipPostingId }
    }

    override fun countByStatus(status: InternshipPosting.PostingStatus): Long =
        viewRepository.findByStatus(status.name).size.toLong()

    override fun getAllPublishedPostings(): List<InternshipPostingView> {
        return viewRepository.findByStatus("PUBLISHED")
    }

    override fun getAllPostings(): List<InternshipPostingView> {
        return viewRepository.findAll()
    }

    override fun getRemotePostings(): List<InternshipPostingView> {
        return viewRepository.findByLocationIsRemoteTrue()
    }
}