package net.medrag.vocabot.modes

import mu.KotlinLogging
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 20.04.2022
 */
// @Component
class ChatMode : Mode {
    override fun name(): ModeName = ModeName.DEFAULT
    override fun processNonCommandUpdate(update: Update?) {
        update?.message?.let {
            if (it.chat?.isUserChat == true) {
                logger.info { "User <${it.chat.userName}> sent message <${it.text}>." }
            }
        }
    }
}

private val logger = KotlinLogging.logger { }
