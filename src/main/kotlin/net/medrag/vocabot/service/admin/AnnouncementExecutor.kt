package net.medrag.vocabot.service.admin

import mu.KotlinLogging
import net.medrag.vocabot.bot.argsToString
import net.medrag.vocabot.dao.SubscriptionRepository
import net.medrag.vocabot.model.CommanderInfo
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
@Component
class AnnouncementExecutor(
    private val subscriptionRepository: SubscriptionRepository
) : AdminCommandExecutor {

    override fun command(): String = "announce"

    override fun execute(commanderInfo: CommanderInfo) {
        commanderInfo.arguments?.argsToString(1)?.let {
            logger.info { "Announcement will be published: \n$it" }
            subscriptionRepository.findAll().forEach { sub ->
                commanderInfo.absSender?.executeAsync(
                    SendMessage.builder().chatId(sub.subId.toString()).text(it).build()
                )?.thenAcceptAsync {
                    commanderInfo.absSender.executeAsync(
                        PinChatMessage.builder().chatId(it?.chatId.toString()).messageId(it?.messageId ?: 0).build()
                    )
                }
            }
        }
    }
}

private val logger = KotlinLogging.logger { }
