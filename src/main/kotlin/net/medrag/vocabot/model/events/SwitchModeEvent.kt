package net.medrag.vocabot.model.events

import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.modes.ModeName
import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class SwitchModeEvent(
    source: Any,
    val mode: ModeName,
    val commanderInfo: CommanderInfo
) : ApplicationEvent(source)
