package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent
import org.telegram.telegrambots.meta.api.methods.GetFile

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class DicLoadEvent(
    source: Any,
    val file: GetFile
) : ApplicationEvent(source)
