package net.medrag.vocabot.model.events

import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class PostQuizEvent(
    source: Any
) : ApplicationEvent(source)
