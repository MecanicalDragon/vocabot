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
class CheckWordsCommand(
    private val checkWordsService: CheckWordsService,
    private val wordsCheckingProps: WordsCheckingProps
) : AbstractCommand(
    "check",
    "Get words from vocabulary to check yourself"
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val number: Int = try {
            arguments?.get(0)?.toInt()?.let {
                if (it > wordsCheckingProps.max) {
                    logger.info {
                        "$it words have been requested, but ${wordsCheckingProps.max} is max. ${wordsCheckingProps.max} will be returned."
                    }
                    wordsCheckingProps.max
                } else {
                    logger.info { "$it words are going to be sent." }
                    it
                }
            } ?: wordsCheckingProps.default
        } catch (e: Exception) {
            logger.info { "Invalid argument accepted for words list size. Default ${wordsCheckingProps.default} will be used." }
            wordsCheckingProps.default
        }
        for (sendMessage in checkWordsService.getWordsToCheck(number, chat.idString())) {
            absSender?.execute(sendMessage)
        }
    }
}

private val logger = KotlinLogging.logger { }
