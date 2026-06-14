package mk.ukim.finki.internshipmanagement.domain.internshipposting


enum class PostingStatus {
    DRAFT,
    PUBLISHED,
    CLOSED;
    
    fun canPublish(): Boolean = this == DRAFT
    
    fun canClose(): Boolean = this == PUBLISHED
    
    fun canUpdate(): Boolean = this == DRAFT || this == PUBLISHED
}

