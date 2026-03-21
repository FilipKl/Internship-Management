package mk.ukim.finki.internshipmanagement.application.internshipposting.query

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * JPA Repository for InternshipPostingView (read model).
 * Provides query methods for reading denormalized internship posting data.
 */
@Repository
interface InternshipPostingViewRepository : JpaRepository<InternshipPostingView, String> {

    // ==================== Status ====================
    fun findByStatus(status: String): List<InternshipPostingView>

    // ==================== Company ====================
    fun findByCompanyNameContainingIgnoreCase(companyName: String): List<InternshipPostingView>



    // ==================== Job Title ====================
    fun findByJobTitleContainingIgnoreCase(jobTitle: String): List<InternshipPostingView>

    // ==================== Tech Stack ====================
    fun findByTechStackContainingIgnoreCase(techStack: String): List<InternshipPostingView>



    // ==================== Location ====================
    fun findByLocationCountryContainingIgnoreCaseAndLocationCityContainingIgnoreCase(
        country: String,
        city: String
    ): List<InternshipPostingView>

    fun findByLocationIsRemoteTrue(): List<InternshipPostingView>

    // Optional: search by either city OR country OR remote
    fun findByLocationCityContainingIgnoreCase(city: String): List<InternshipPostingView>
    fun findByLocationCountryContainingIgnoreCase(country: String): List<InternshipPostingView>

    // ==================== Combined ====================
    fun findByStatusAndCompanyNameContainingIgnoreCase(
        status: String,
        companyName: String
    ): List<InternshipPostingView>
}