package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.bot.bold
import net.medrag.vocabot.bot.italic
import net.medrag.vocabot.bot.toNewLinedString
import net.medrag.vocabot.bot.underline
import net.medrag.vocabot.config.DailyProps
import net.medrag.vocabot.dao.IdiomRepository
import net.medrag.vocabot.dao.IrregularVerbRepository
import net.medrag.vocabot.dao.PhrasalVerbRepository
import net.medrag.vocabot.model.events.PostEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.random.Random

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
@Service
class DailyService(
    private val phrasalVerbRepository: PhrasalVerbRepository,
    private val irregularRepository: IrregularVerbRepository,
    private val idiomRepository: IdiomRepository,
    private val publisher: ApplicationEventPublisher,
    private val dailyProps: DailyProps
) {

    @Scheduled(cron = "\${net.medrag.vocabot.daily.cron}")
    fun sendDaily() {
        when (DailyEvent.random()) {
            DailyEvent.IRREGULAR -> prepareIrregular()
            DailyEvent.IDIOM -> prepareIdiom()
            DailyEvent.PHRASAL -> preparePhrasalWord()
        }.also {
            publisher.publishEvent(it)
        }
    }

    private fun prepareIrregular(): PostEvent {
        logger.info { "Irregular word set is going to be sent." }
        val irregulars = irregularRepository.findSome()

        val message = StringBuilder("Here are irregular verbs for today that are related with a sign:\n")
            .append(irregulars[0].description.underline()).append("\n\n")
        for (verb in irregulars) {
            message.append(verb).append("\n")
        }
        return PostEvent(message.toString(), true, this)
    }

    private fun prepareIdiom(): PostEvent {
        logger.info { "Idiom set is going to be sent." }
        val idioms = idiomRepository.findSome(dailyProps.idioms)

        val message = StringBuilder("Here are some idioms for today:\n\n")
        for (idiom in idioms) {
            message
                .append(idiom.idiom.bold()).append("\n")
                .append(idiom.meaning).append("\n")
            if (idiom.examples.isNotEmpty()) {
                message.append(idiom.examples.toNewLinedString().italic()).append("\n")
            }
            message.append("\n")
        }
        return PostEvent(message.toString(), true, this)
    }

    private fun preparePhrasalWord(): PostEvent {
        logger.info { "Phrasal word set is going to be sent." }
        val phrasalList = phrasalVerbRepository.findSome(dailyProps.phrasalVerbs)

        val message = StringBuilder("Here are some phrasal verbs for today:\n\n")
        for (phrasalVerb in phrasalList) {
            message
                .append(phrasalVerb.verb.bold()).append("\n")
                .append(phrasalVerb.meaning).append("\n")
                .append(phrasalVerb.examples.italic()).append("\n\n")
        }
        return PostEvent(message.toString(), true, this)
    }
}

private enum class DailyEvent {
    PHRASAL,
    IDIOM,
    IRREGULAR;

    companion object {
        fun random(): DailyEvent {
            val random = Random.nextInt(DailyEvent.values().size)
            return DailyEvent.values()[random]
        }
    }
}

private val logger = KotlinLogging.logger { }
