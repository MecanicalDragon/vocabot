package net.medrag.vocabot.command

import net.medrag.vocabot.config.DailyProps
import net.medrag.vocabot.service.IdiomService
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
class GetIdiomCommand(
    private val idiomService: IdiomService,
    private val dailyProps: DailyProps
) : AbstractCommand(
    "idioms",
    "Get a list of random idioms",
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val message = idiomService.getIdiomHtmlMessage(dailyProps.idioms)
        absSender?.execute(
            SendMessage().apply {
                chatId = chat?.id.toString()
                text = message
                parseMode = "HTML"
            }
        )
    }
}
