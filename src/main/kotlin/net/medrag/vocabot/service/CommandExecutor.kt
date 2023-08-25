package net.medrag.vocabot.service

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Stanislav Tretiakov
 * 25.08.2023
 */
interface CommandExecutor {
    fun <T : Serializable?, Method : BotApiMethod<T>?> sendAsync(method: Method?): CompletableFuture<T>?

    fun <T : Serializable?, Method : BotApiMethod<T>?> send(method: Method?): T
}
