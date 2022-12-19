package net.medrag.vocabot.model.exceptions

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
class WordAlreadyExistsException(override val message: String) : RuntimeException(message)
