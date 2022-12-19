package net.medrag.vocabot.command

import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.model.events.SwitchModeEvent
import net.medrag.vocabot.modes.ModeName
import org.springframework.context.ApplicationEventPublisher
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
//@Component
class SwitchModeCommand(
    private val publisher: ApplicationEventPublisher
) : AbstractCommand(
    "switch",
    "Switch bot mode. Unavailable for mortals."
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        publisher.publishEvent(
            SwitchModeEvent(
                this,
                ModeName.withTag(arguments?.get(0) ?: "default"),
                CommanderInfo(absSender, user, chat, arguments)
            )
        )
    }
}
