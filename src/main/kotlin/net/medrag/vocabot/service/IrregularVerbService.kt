package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.bot.underline
import net.medrag.vocabot.dao.IrregularVerbRepository
import org.springframework.stereotype.Service
import kotlin.random.Random


/**
 * @author Stanislav Tretiakov
 * 10.09.2024
 */
@Service
class IrregularVerbService(
    private val irregularVerbRepository: IrregularVerbRepository
) {
    fun getIrregularVerbsHtmlMessage(): String {
        logger.info { "Irregular word set is going to be sent." }
//        val irregulars = irregularVerbRepository.findSome()
        val patterns = irregularVerbRepository.getPatternsCount()
        val irregulars = irregularVerbRepository.findAllByPattern(Random.nextInt(1, patterns + 1))
        val message = StringBuilder("Here are irregular verbs united by a sign:\n")
            .append(irregulars[0].description.underline()).append("\n\n")
        for (verb in irregulars) {
            message.append(verb).append("\n")
        }
        return message.toString()
    }
}

private val logger = KotlinLogging.logger { }
