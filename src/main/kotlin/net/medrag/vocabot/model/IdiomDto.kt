package net.medrag.vocabot.model

import net.medrag.vocabot.bot.toNewLinedString

/**
 * @author Stanislav Tretyakov
 * 19.12.2022
 */
data class IdiomDto(
    val idiom: String,
    val meaning: String,
    val examples: List<String> = emptyList()
) {
    override fun toString() = "Idiom:\n" +
        "${idiom}\n" +
        "${meaning}\n" +
        if (examples.isEmpty()) "" else examples.toNewLinedString()
}
