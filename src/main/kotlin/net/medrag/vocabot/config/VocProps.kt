package net.medrag.vocabot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Stanislav Tretyakov
 * 17.05.2022
 */
@ConfigurationProperties(prefix = "net.medrag.vocabot.voc")
data class VocProps(
    val wordsInQuiz: Int = 5,
    val delimiter: String = ";",
    val useLike: Boolean = false
)
