package net.medrag.vocabot.command

import net.medrag.vocabot.bot.idString
import net.medrag.vocabot.service.CheckWordsService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
@Component
class CheckWordsCommand(
    private val checkWordsService: CheckWordsService
) : AbstractCommand(
    "check",
    "Get words from vocabulary to check yourself"
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val number: Int = try {
            arguments?.get(0)?.toInt() ?: DEFAULT_SIZE
        } catch (e: Exception) {
            DEFAULT_SIZE
        }
        for (sendMessage in checkWordsService.getWordsToCheck(number, chat.idString())) {
            absSender?.execute(sendMessage)
        }
    }

    companion object {
        private const val DEFAULT_SIZE = 10
    }
}
