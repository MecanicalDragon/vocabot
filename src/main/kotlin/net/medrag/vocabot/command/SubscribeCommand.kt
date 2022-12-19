package net.medrag.vocabot.command

import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.service.SubscriptionService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class SubscribeCommand(
    private val subscriptionService: SubscriptionService
) : AbstractCommand(
    "sub",
    "Subscribe scheduled words distribution"
) {
    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        subscriptionService.subscribe(CommanderInfo(absSender, user, chat, arguments))
    }
}
