package net.medrag.vocabot

import mu.KotlinLogging
import net.medrag.vocabot.config.DailyProps
import net.medrag.vocabot.config.MasterProps
import net.medrag.vocabot.config.VocProps
import net.medrag.vocabot.config.WordsCheckingProps
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author Stanislav Tretyakov
 * 17.04.2022
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = [MasterProps::class, VocProps::class, DailyProps::class, WordsCheckingProps::class])
class VocaBotApp : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        logger.info("VocaBot is running!")
    }
}

fun main(args: Array<String>) {
    runApplication<VocaBotApp>(*args)
}

private val logger = KotlinLogging.logger { }
