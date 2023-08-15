package net.medrag.vocabot.callback

import net.medrag.vocabot.bot.CALLBACK_PREFIX_ADD_TO_LEARN
import net.medrag.vocabot.bot.CALLBACK_PREFIX_REMOVE_FROM_LEARN
import net.medrag.vocabot.model.events.CheckLearnEvent
import net.medrag.vocabot.model.events.LearnType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
@Component
class AddWordToLearnCallbackExecutor(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        applicationEventPublisher.publishEvent(CheckLearnEvent(update, extractCallbackPostfix(update).toInt(), LearnType.TO_LEARN, this))
        return null
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_ADD_TO_LEARN
}

@Component
class MarrWordAsLearnedCallbackExecutor(
    private val applicationEventPublisher: ApplicationEventPublisher
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        applicationEventPublisher.publishEvent(CheckLearnEvent(update, extractCallbackPostfix(update).toInt(), LearnType.LEARNED, this))
        return null
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_REMOVE_FROM_LEARN
}
