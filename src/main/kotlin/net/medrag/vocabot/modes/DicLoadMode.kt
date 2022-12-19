package net.medrag.vocabot.modes

import mu.KotlinLogging
import net.medrag.vocabot.model.events.DicLoadEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class DicLoadMode(
    private val publisher: ApplicationEventPublisher
) : Mode {

    override fun name(): ModeName = ModeName.LOAD_DIC

    override fun processNonCommandUpdate(update: Update?) {
        update?.message?.document?.fileId?.let {
            publisher.publishEvent(DicLoadEvent(this, GetFile(it)))
        } ?: logger.warn { "Load mode is turned on, but message doesn't contain vocabulary attachment." }
    }
}

private val logger = KotlinLogging.logger { }
