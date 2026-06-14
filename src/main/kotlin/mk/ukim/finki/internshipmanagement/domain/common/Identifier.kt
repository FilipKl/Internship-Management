package mk.ukim.finki.internshipmanagement.domain.common


interface Identifier<T> {
    fun getValue(): T
}

