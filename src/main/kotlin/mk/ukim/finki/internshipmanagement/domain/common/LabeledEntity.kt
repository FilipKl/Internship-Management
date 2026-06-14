package mk.ukim.finki.internshipmanagement.domain.common


    interface LabeledEntity {
        fun getId(): Identifier<*>
        fun getLabel(): String
    }