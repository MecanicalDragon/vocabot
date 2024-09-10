package net.medrag.vocabot.service

import net.medrag.vocabot.config.DailyProps
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
    private val phrasalVerbService: PhrasalVerbService,
    private val irregularVerbService: IrregularVerbService,
    private val idiomService: IdiomService,
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
        val message = irregularVerbService.getIrregularVerbsHtmlMessage()
        return PostEvent(message, true, this)
    }

    private fun prepareIdiom(): PostEvent {
        val message = idiomService.getIdiomHtmlMessage(dailyProps.idioms)
        return PostEvent(message, true, this)
    }

    private fun preparePhrasalWord(): PostEvent {
        val message = phrasalVerbService.getPhrasalVerbsHtmlMessage(dailyProps.phrasalVerbs)
        return PostEvent(message, true, this)
    }
}

private enum class DailyEvent {
    PHRASAL,
    IDIOM,
    IRREGULAR;

    companion object {
        fun random(): DailyEvent {
            val random = Random.nextInt(entries.size)
            return entries[random]
        }
    }
}
