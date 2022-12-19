package net.medrag.vocabot.model.events

import net.medrag.vocabot.model.IrregularVerbDto
import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class PostIrregularVerbEvent(
    val verb: IrregularVerbDto,
    source: Any
) : ApplicationEvent(source)
