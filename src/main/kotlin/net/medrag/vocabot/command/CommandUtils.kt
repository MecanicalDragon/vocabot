package net.medrag.vocabot.command

import mu.KotlinLogging

/**
 * @author Stanislav Tretiakov
 * 19.08.2023
 */
fun parseArgAsInt(arguments: Array<out String>?, argNumber: Int = 0, defaultValue: Int = 0): Int {
    return try {
        arguments?.get(argNumber)?.toInt() ?: defaultValue
    } catch (e: Exception) {
        logger.info { "Null or invalid int argument has been passed as a <$argNumber> argument. Default value <$defaultValue> will be returned." }
        defaultValue
    }
}

private val logger = KotlinLogging.logger { }
