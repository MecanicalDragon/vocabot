package net.medrag.vocabot.modes

import mu.KotlinLogging
import net.medrag.vocabot.bot.callbackPrefix
import net.medrag.vocabot.callback.CallbackExecutionResult
import net.medrag.vocabot.callback.CallbackExecutor
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class DefaultMode(
    _callbacks: List<CallbackExecutor>
) : Mode {
    private val callbacks = _callbacks.associateBy { it.getCallbackPrefix() }

    override fun name(): ModeName = ModeName.DEFAULT

    override fun processNonCommandUpdate(update: Update?): CallbackExecutionResult? {
        return if (update?.hasCallbackQuery() == true) {
            callbacks[update.callbackPrefix()]?.executeCallback(update)
        } else {
            update?.message?.let {
                if (it.chat?.isUserChat == true) {
                    logger.info { "User <${it.chat.userName}> sent message <${it.text}>." }
                }
            }
            null
        }
    }
}

private val logger = KotlinLogging.logger { }
