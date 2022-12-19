package net.medrag.vocabot.service.admin

import mu.KotlinLogging
import net.medrag.vocabot.dao.SubscriptionRepository
import net.medrag.vocabot.model.CommanderInfo
import org.springframework.stereotype.Component

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
@Component
class PrintSubsExecutor(
    private val subscriptionRepository: SubscriptionRepository
) : AdminCommandExecutor {

    override fun command(): String = "subs"

    override fun execute(commanderInfo: CommanderInfo) {
        logger.info { "Current subscriptions:" }
        subscriptionRepository.findAll().forEach {
            logger.info { it }
        }
    }
}

private val logger = KotlinLogging.logger { }
