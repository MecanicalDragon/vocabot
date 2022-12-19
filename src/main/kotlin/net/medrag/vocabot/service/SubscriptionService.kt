package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.dao.ChatType
import net.medrag.vocabot.dao.Subscription
import net.medrag.vocabot.dao.SubscriptionRepository
import net.medrag.vocabot.model.CommanderInfo
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository
) {

    fun getSubscriptions(): List<Subscription> {
        return subscriptionRepository.findAll()
    }

    fun subscribe(commanderInfo: CommanderInfo) {

        logger.trace {
            "New subscription request has been received. User id: <${commanderInfo.user?.id}>, " +
                "user name: <${commanderInfo.user?.userName}>, chat id: <${commanderInfo.chat?.id}>," +
                "chat name: <${commanderInfo.chat?.userName}>."
        }
        val subscription = buildSubscription(commanderInfo)
        subscriptionRepository.save(subscription)
        logger.info { "Subscription has been created: <$subscription>." }
        commanderInfo.absSender?.execute(
            SendMessage().apply {
                chatId = subscription.subId.toString()
                text = "subscribed"
            }
        )
    }

    fun unsubscribe(commanderInfo: CommanderInfo) {

        logger.trace {
            "New unsubscription request has been received. User id: <${commanderInfo.user?.id}>, " +
                "user name: <${commanderInfo.user?.userName}>, chat id: <${commanderInfo.chat?.id}>," +
                "chat name: <${commanderInfo.chat?.userName}>."

            val subscription = buildSubscription(commanderInfo)
            subscriptionRepository.delete(subscription)
            logger.info { "Subscription has been removed: <$subscription>." }
            commanderInfo.absSender?.execute(
                SendMessage().apply {
                    chatId = subscription.subId.toString()
                    text = "unsubscribed"
                }
            )
        }
    }

    private fun buildSubscription(commanderInfo: CommanderInfo) = if (commanderInfo.chat?.userName == null) {
        Subscription(
            commanderInfo.chat?.id ?: throw IllegalArgumentException("invalid chat id"),
            ChatType.CHAT,
            commanderInfo.chat.title
        )
    } else {
        Subscription(
            commanderInfo.user?.id ?: throw IllegalArgumentException("invalid user id"),
            ChatType.USER,
            commanderInfo.user.userName
        )
    }
}

private val logger = KotlinLogging.logger { }
