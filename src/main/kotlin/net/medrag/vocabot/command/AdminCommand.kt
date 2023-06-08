package net.medrag.vocabot.command

import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.model.events.AdministrationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
@Component
class AdminCommand(
    private val publisher: ApplicationEventPublisher
) : AbstractCommand(
    "admin",
    "Administration commands. Unavailable for mortals."
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        publisher.publishEvent(
            AdministrationEvent(
                this,
                CommanderInfo(absSender, user, chat, arguments)
            )
        )
    }
}
