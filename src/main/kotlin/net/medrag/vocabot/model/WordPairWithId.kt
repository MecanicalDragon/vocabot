package net.medrag.vocabot.model

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
data class WordPairWithId(
    val id: Int,
    val word1: String,
    val word2: String,
    val examples: List<String> = emptyList()
) {
    override fun toString() = "$word1 - $word2"
}
