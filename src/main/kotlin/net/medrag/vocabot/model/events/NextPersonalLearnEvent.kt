package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
class NextPersonalLearnEvent(
    val update: Update,
    val number: Int,
    val type: Type,
    source: Any
) : ApplicationEvent(source)

enum class Type {
    CHECK, LEARN
}
