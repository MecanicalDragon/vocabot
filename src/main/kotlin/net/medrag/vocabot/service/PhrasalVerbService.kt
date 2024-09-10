package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.bot.bold
import net.medrag.vocabot.bot.italic
import net.medrag.vocabot.dao.PhrasalVerbRepository
import org.springframework.stereotype.Service

/**
 * @author Stanislav Tretiakov
 * 10.09.2024
 */
@Service
class PhrasalVerbService(
    private val phrasalVerbRepository: PhrasalVerbRepository,
) {
    fun getPhrasalVerbsHtmlMessage(number: Int): String {
        logger.info { "Phrasal word set of size <$number> is going to be sent." }
        val phrasalList = phrasalVerbRepository.findSome(number)

        val message = StringBuilder("Here are some phrasal verbs:\n\n")
        for (phrasalVerb in phrasalList) {
            message
                .append(phrasalVerb.verb.bold()).append("\n")
                .append(phrasalVerb.meaning).append("\n")
                .append(phrasalVerb.examples.italic()).append("\n\n")
        }
        return message.toString()
    }
}

private val logger = KotlinLogging.logger { }
