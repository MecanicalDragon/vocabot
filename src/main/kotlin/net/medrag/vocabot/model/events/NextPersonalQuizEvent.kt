package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 27.04.2022
 */
class NextPersonalQuizEvent(
    val update: Update,
    val answer: Answer,
    source: Any
) : ApplicationEvent(source) {
    enum class Answer {
        YES,
        NO
    }
}
