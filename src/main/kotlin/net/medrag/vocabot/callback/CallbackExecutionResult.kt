package net.medrag.vocabot.callback

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import java.io.Serializable

/**
 * @author Stanislav Tretyakov
 * 07.02.2021
 */
data class CallbackExecutionResult(
    val replyActions: List<ReplyAction> = emptyList()
)

data class ReplyAction(
    val botApiMethod: BotApiMethod<Serializable>,
    val executionWay: ExecutionWay
)

enum class ExecutionWay {
    DEFAULT,
    ASYNC
}

fun defaultReplyAction(botApiMethod: BotApiMethod<Serializable>) = ReplyAction(botApiMethod, ExecutionWay.DEFAULT)

fun asyncReplyAction(botApiMethod: BotApiMethod<Serializable>) = ReplyAction(botApiMethod, ExecutionWay.ASYNC)

fun replyCallback(callbackQueryId: String) = asyncReplyAction(AnswerCallbackQuery(callbackQueryId) as BotApiMethod<Serializable>)

fun deleteMessage(chatIdentifier: String, messageIdentifier: Int) = asyncReplyAction(
    DeleteMessage().apply {
        chatId = chatIdentifier
        messageId = messageIdentifier
    } as BotApiMethod<Serializable>
)

fun deleteReplyMarkup(chatIdentifier: String, messageIdentifier: Int) =
    asyncReplyAction(EditMessageReplyMarkup(chatIdentifier, messageIdentifier, null, null))
