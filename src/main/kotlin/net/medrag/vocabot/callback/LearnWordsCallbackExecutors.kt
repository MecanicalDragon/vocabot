package net.medrag.vocabot.callback

import mu.KotlinLogging
import net.medrag.vocabot.bot.CALLBACK_PREFIX_ADD_TO_LEARN
import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_CHECK
import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_LEARN
import net.medrag.vocabot.bot.CALLBACK_PREFIX_REMOVE_FROM_LEARN
import net.medrag.vocabot.service.CheckWordsService
import net.medrag.vocabot.service.SubscriptionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.Serializable

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
@Component
class AddWordToLearnCallbackExecutor(
    private val subscriptionService: SubscriptionService
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        val chatIdentifier = update.callbackQuery.message.chatId.toString()
        val wordId = extractCallbackPostfix(update).toInt()
        subscriptionService.addToLearn(chatIdentifier, wordId)

        logger.info { "Word with id <$wordId> has been added for learning for the subscriber with chat id <$chatIdentifier>." }

        return CallbackExecutionResult(
            listOf(
                replyCallback(update.callbackQuery.id),
                deleteReplyMarkup(chatIdentifier, update.callbackQuery.message.messageId)
            )
        )
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_ADD_TO_LEARN
}

@Component
class MarkWordAsLearnedCallbackExecutor(
    private val subscriptionService: SubscriptionService
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        val chatIdentifier = update.callbackQuery.message.chatId.toString()
        val wordId = extractCallbackPostfix(update).toInt()
        subscriptionService.learned(chatIdentifier, wordId)

        logger.info { "Word with id <$wordId> has been removed from learning for the subscriber with chat id <$chatIdentifier>." }

        return CallbackExecutionResult(
            listOf(
                replyCallback(update.callbackQuery.id),
                deleteReplyMarkup(chatIdentifier, update.callbackQuery.message.messageId)
            )
        )
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_REMOVE_FROM_LEARN
}

@Component
class GetWordsToLearnCallbackExecutor(
    private val checkWordsService: CheckWordsService
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        val chatIdentifier = update.callbackQuery.message.chatId.toString()
        val number = extractCallbackPostfix(update).toInt()
        val replyActions = if (number > 0) {
            val words = checkWordsService.getWordsToLearn(chatIdentifier, number)
            words.map { defaultReplyAction(it as BotApiMethod<Serializable>) }
        } else {
            emptyList()
        }

        logger.info { "Next <$number> words are going to be sent for learning to the subscriber with chat id <$chatIdentifier>." }

        return CallbackExecutionResult(
            listOf(
                replyCallback(update.callbackQuery.id),
                deleteMessage(chatIdentifier, update.callbackQuery.message.messageId)
            ) + replyActions
        )
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_GET_LEARN
}

@Component
class CheckLearnCandidatesCallbackExecutor(
    private val checkWordsService: CheckWordsService
) : CallbackExecutor {
    override fun executeCallback(update: Update): CallbackExecutionResult? {
        val chatIdentifier = update.callbackQuery.message.chatId.toString()
        val number = extractCallbackPostfix(update).toInt()
        val replyActions = if (number > 0) {
            val words = checkWordsService.getWordsToCheck(chatIdentifier, number)
            words.map { defaultReplyAction(it as BotApiMethod<Serializable>) }
        } else {
            emptyList()
        }

        logger.info { "Next <$number> words are going to be sent to check for learning to the subscriber with chat id <$chatIdentifier>." }

        return CallbackExecutionResult(
            listOf(
                replyCallback(update.callbackQuery.id),
                deleteMessage(chatIdentifier, update.callbackQuery.message.messageId)
            ) + replyActions
        )
    }

    override fun getCallbackPrefix(): String = CALLBACK_PREFIX_GET_CHECK
}

private val logger = KotlinLogging.logger {}
