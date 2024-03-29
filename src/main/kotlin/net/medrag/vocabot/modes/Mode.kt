package net.medrag.vocabot.modes

import net.medrag.vocabot.callback.CallbackExecutionResult
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface Mode {
    fun name(): ModeName
    fun processNonCommandUpdate(update: Update?): CallbackExecutionResult?
}

enum class ModeName(
    private val tag: String
) {
    DEFAULT("default");

    companion object {
        fun withTag(tag: String?): ModeName {
            return tag?.let {
                ModeName.values().find { it.tag == tag } ?: DEFAULT
            } ?: DEFAULT
        }
    }
}
