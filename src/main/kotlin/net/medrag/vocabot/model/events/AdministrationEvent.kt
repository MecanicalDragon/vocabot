package net.medrag.vocabot.model.events

import net.medrag.vocabot.model.CommanderInfo
import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
class AdministrationEvent(
    source: Any,
    val commanderInfo: CommanderInfo
) : ApplicationEvent(source)
