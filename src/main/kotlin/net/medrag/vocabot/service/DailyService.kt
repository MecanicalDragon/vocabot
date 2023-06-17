package net.medrag.vocabot.service

import net.medrag.vocabot.config.DailyProps
import net.medrag.vocabot.dao.PhrasalVerbRepository
import net.medrag.vocabot.model.events.PostEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
@Service
class DailyService(
    private val phrasalVerbRepository: PhrasalVerbRepository,
    private val publisher: ApplicationEventPublisher,
    private val dailyProps: DailyProps
) {

    @Scheduled(cron = "\${net.medrag.vocabot.daily.cron}")
    fun sendDaily() {
        val phrasalList = phrasalVerbRepository.findSome(dailyProps.phrasalVerbs)

        val message = StringBuilder("Here are some phrasal verbs for today:\n\n")
        for (phrasalVerb in phrasalList) {
            message
                .append(phrasalVerb.verb).append("\n")
                .append(phrasalVerb.meaning).append("\n")
                .append(phrasalVerb.examples).append("\n\n")
        }
        val event = PostEvent(message.toString(), this)
        publisher.publishEvent(event)
    }
}
