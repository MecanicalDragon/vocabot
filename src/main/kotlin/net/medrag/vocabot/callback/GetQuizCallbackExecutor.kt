package net.medrag.vocabot.callback

import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_QUIZ
import net.medrag.vocabot.model.events.NextPersonalQuizEvent
import net.medrag.vocabot.model.events.NextPersonalQuizEvent.Answer
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 08.02.2021
 */
@Component
class GetQuizCallbackExecutor(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        if (extractCallbackPostfix(update) == NextPersonalQuizEvent.Answer.YES.toString()) {
            applicationEventPublisher.publishEvent(NextPersonalQuizEvent(update, Answer.YES, this))
        } else {
            applicationEventPublisher.publishEvent(NextPersonalQuizEvent(update, Answer.NO, this))
        }
        return null
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_GET_QUIZ
}
