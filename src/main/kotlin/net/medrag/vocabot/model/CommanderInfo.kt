package net.medrag.vocabot.model

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
data class CommanderInfo(
    val absSender: AbsSender? = null,
    val user: User? = null,
    val chat: Chat? = null,
    val arguments: Array<out String>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommanderInfo

        if (absSender != other.absSender) return false
        if (user != other.user) return false
        if (chat != other.chat) return false
        if (arguments != null) {
            if (other.arguments == null) return false
            if (!arguments.contentEquals(other.arguments)) return false
        } else if (other.arguments != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = absSender?.hashCode() ?: 0
        result = 31 * result + (user?.hashCode() ?: 0)
        result = 31 * result + (chat?.hashCode() ?: 0)
        result = 31 * result + (arguments?.contentHashCode() ?: 0)
        return result
    }
}
