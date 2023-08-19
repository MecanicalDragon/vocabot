package net.medrag.vocabot.command

import mu.KotlinLogging
import net.medrag.vocabot.bot.idString
import net.medrag.vocabot.config.WordsCheckingProps
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
class GetWordsCommand(
    private val checkWordsService: CheckWordsService,
    private val wordsCheckingProps: WordsCheckingProps
) : AbstractCommand(
    "get",
    "Get words from vocabulary to check yourself"
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val number: Int = parseArgAsInt(arguments, 0, wordsCheckingProps.default).let {
            if (it > wordsCheckingProps.max) {
                logger.info { "<$it> words have been requested to check, but <${wordsCheckingProps.max}> is max." }
                wordsCheckingProps.max
            } else {
                it
            }
        }
        logger.info { "<$number> words are going to be sent to check." }
        for (sendMessage in checkWordsService.getWordsToCheck(chat.idString(), number)) {
            absSender?.execute(sendMessage)
        }
    }
}

private val logger = KotlinLogging.logger { }
