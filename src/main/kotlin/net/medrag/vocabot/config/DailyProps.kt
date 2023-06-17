package net.medrag.vocabot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
@ConfigurationProperties("net.medrag.vocabot.daily")
data class DailyProps(
    val phrasalVerbs: Int
)
