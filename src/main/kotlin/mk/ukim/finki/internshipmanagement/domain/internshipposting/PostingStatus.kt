package mk.ukim.finki.internshipmanagement.domain.internshipposting

/**
 * Enum representing the status of an internship posting.
 * 
 * States:
 * - DRAFT: Posting created but not yet published
 * - PUBLISHED: Posting is active and visible to candidates
 * - CLOSED: Posting is closed and no longer accepting applications
 */
enum class PostingStatus {
    DRAFT,
    PUBLISHED,
    CLOSED;
    
    fun canPublish(): Boolean = this == DRAFT
    
    fun canClose(): Boolean = this == PUBLISHED
    
    fun canUpdate(): Boolean = this == DRAFT || this == PUBLISHED
}

