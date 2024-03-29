package net.medrag.vocabot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Stanislav Tretyakov
 * 07.03.2021
 */
@ConfigurationProperties(prefix = "net.medrag.vocabot.master")
data class MasterProps(
    val botName: String,
    val token: String,
    val master: String,
    val sourceChat: String
)
