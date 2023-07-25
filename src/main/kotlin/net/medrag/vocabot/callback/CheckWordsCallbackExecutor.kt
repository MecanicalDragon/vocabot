package net.medrag.vocabot.callback

import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_LEARN
import net.medrag.vocabot.model.events.NextPersonalLearnEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
@Component
class CheckWordsCallbackExecutor(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        applicationEventPublisher.publishEvent(NextPersonalLearnEvent(update, extractCallbackPostfix(update).toInt(), this))
        return null
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_GET_LEARN
}
