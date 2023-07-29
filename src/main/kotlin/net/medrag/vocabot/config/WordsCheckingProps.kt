package net.medrag.vocabot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Stanislav Tretiakov
 * 26.07.2023
 */
@ConfigurationProperties(prefix = "net.medrag.vocabot.words-checking")
data class WordsCheckingProps(
    val default: Int,
    val max: Int
)
