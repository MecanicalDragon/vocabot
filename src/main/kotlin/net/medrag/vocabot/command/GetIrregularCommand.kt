package net.medrag.vocabot.command

import net.medrag.vocabot.service.IrregularVerbService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretiakov
 * 10.09.2024
 */
@Component
class GetIrregularCommand(
    private val irregularVerbService: IrregularVerbService
) : AbstractCommand(
    "irregular",
    "Get a list of irregular verbs united by a specific sign",
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val verbsMessage = irregularVerbService.getIrregularVerbsHtmlMessage()
        absSender?.execute(
            SendMessage().apply {
                chatId = chat?.id.toString()
                text = verbsMessage
                parseMode = "HTML"
            }
        )
    }
}
