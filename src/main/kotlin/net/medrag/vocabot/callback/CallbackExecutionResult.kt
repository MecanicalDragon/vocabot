package net.medrag.vocabot.callback

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.io.Serializable

/**
 * @author Stanislav Tretyakov
 * 07.02.2021
 */
interface CallbackExecutionResult {
    fun getExecutable(): BotApiMethod<Serializable>? = null
}
