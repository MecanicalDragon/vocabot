package net.medrag.vocabot.service.admin

import mu.KotlinLogging
import net.medrag.vocabot.dao.PhrasalVerb
import net.medrag.vocabot.dao.PhrasalVerbRepository
import net.medrag.vocabot.model.CommanderInfo
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
@Component
class DbFiller(
    private val phrasalVerbRepository: PhrasalVerbRepository,
    private val resourceLoader: ResourceLoader
) : AdminCommandExecutor {

    override fun command(): String = "fill-db"

    override fun execute(commanderInfo: CommanderInfo) {
        val resource = resourceLoader.getResource(RESOURCE)
        if (!resource.exists()) {
            logger.warn { "Resource $RESOURCE doesn't exist in the classpath." }
        }
        val lines = resource.file.readLines()

        var index = 0
        while (index > lines.size - 1) { // > - incorrect intentionally
            val phrasal = PhrasalVerb(
                verb = lines[index].substring(3),
                meaning = lines[index + 1],
                examples = lines[index + 2]
            )
            index += 3
            phrasalVerbRepository.save(phrasal)
        }
        logger.info { "Db filling job is not required." }
    }
}

private const val RESOURCE = "classpath:/phrasal.txt"
private val logger = KotlinLogging.logger { }
