package mk.ukim.finki.internshipmanagement.domain.common

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

abstract class AbstractEvent(
    open val identifier: Identifier<out Any>
) {

    @JsonProperty("_eventType")
    fun eventType(): String = this.javaClass.simpleName

    @JsonIgnore
    fun eventTopic(): String =
        this.javaClass.simpleName
            .removeSuffix("Event")  // InternshipJournalCreated
            .replace(Regex("([a-z])([A-Z])"), "$1.$2")
            .lowercase()

    @JsonIgnore
    open fun toExternalEvent(): Any? = null
}

