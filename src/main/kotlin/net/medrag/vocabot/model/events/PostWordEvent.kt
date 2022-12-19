package net.medrag.vocabot.model.events

import net.medrag.vocabot.model.WordPairDto
import org.springframework.context.ApplicationEvent

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class PostWordEvent(
    val wordPair: WordPairDto,
    source: Any
) : ApplicationEvent(source)
