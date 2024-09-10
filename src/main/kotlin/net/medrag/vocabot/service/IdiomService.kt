package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.bot.bold
import net.medrag.vocabot.bot.italic
import net.medrag.vocabot.bot.toNewLinedString
import net.medrag.vocabot.dao.IdiomRepository
import org.springframework.stereotype.Service

/**
 * @author Stanislav Tretiakov
 * 10.09.2024
 */
@Service
class IdiomService(
    private val idiomRepository: IdiomRepository
) {
    fun getIdiomHtmlMessage(number: Int): String {
        logger.info { "Idiom set of size <$number> is going to be sent." }
        val idioms = idiomRepository.findSome(number)

        val message = StringBuilder("Here are some idioms:\n\n")
        for (idiom in idioms) {
            message
                .append(idiom.idiom.bold()).append("\n")
                .append(idiom.meaning).append("\n")
            if (idiom.examples.isNotEmpty()) {
                message.append(idiom.examples.toNewLinedString().italic()).append("\n")
            }
            message.append("\n")
        }
        return message.toString()
    }
}

private val logger = KotlinLogging.logger { }
