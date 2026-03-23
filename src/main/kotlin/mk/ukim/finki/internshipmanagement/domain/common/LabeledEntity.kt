package mk.ukim.finki.internshipmanagement.domain.common

/**
 * Interface for entities that have both an ID and a display label.
 * Used for entities that need to be displayed in UIs or logs.
 */
    interface LabeledEntity {

        /**
         * Get the unique identifier for this entity.
         */
        fun getId(): Identifier<*>

        /**
         * Get a human-readable label for this entity.
         * Used for display purposes.
         */
        fun getLabel(): String
    }