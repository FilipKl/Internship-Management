package mk.ukim.finki.internshipmanagement.domain.common

/**
 * Base interface for strongly-typed identifiers.
 * Provides type safety for aggregate IDs.
 */
interface Identifier<T> {
    fun getValue(): T
}

