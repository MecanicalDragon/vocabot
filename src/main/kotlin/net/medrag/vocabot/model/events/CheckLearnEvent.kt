package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
class CheckLearnEvent(
    val update: Update,
    val wordId: Int,
    val type: LearnType,
    source: Any
) : ApplicationEvent(source)

enum class LearnType {
    TO_LEARN, LEARNED
}
