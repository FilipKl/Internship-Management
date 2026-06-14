package mk.ukim.finki.internshipmanagement.application.internshipposting.query

import mk.ukim.finki.internshipmanagement.domain.internshipposting.InternshipPostingId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InternshipPostingViewRepository : JpaRepository<InternshipPostingView, InternshipPostingId> {

    fun findByStatus(status: String): List<InternshipPostingView>

    fun findByCompanyNameContainingIgnoreCase(companyName: String): List<InternshipPostingView>

    fun findByJobTitleContainingIgnoreCase(jobTitle: String): List<InternshipPostingView>

    fun findByTechStackContainingIgnoreCase(techStack: String): List<InternshipPostingView>


    fun findByLocationCountryContainingIgnoreCaseAndLocationCityContainingIgnoreCase(
        country: String,
        city: String
    ): List<InternshipPostingView>

    fun findByLocationIsRemoteTrue(): List<InternshipPostingView>

    fun findByLocationCityContainingIgnoreCase(city: String): List<InternshipPostingView>
    fun findByLocationCountryContainingIgnoreCase(country: String): List<InternshipPostingView>

    fun findByStatusAndCompanyNameContainingIgnoreCase(
        status: String,
        companyName: String
    ): List<InternshipPostingView>
}