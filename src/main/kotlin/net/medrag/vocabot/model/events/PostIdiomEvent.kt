package net.medrag.vocabot.model.events

import net.medrag.vocabot.model.IdiomDto
import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 19.12.2022
 */
class PostIdiomEvent(
    val idiom: IdiomDto,
    source: Any
) : ApplicationEvent(source)
