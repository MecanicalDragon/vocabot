package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
class PostEvent(
    val message: String,
    source: Any
) : ApplicationEvent(source)
