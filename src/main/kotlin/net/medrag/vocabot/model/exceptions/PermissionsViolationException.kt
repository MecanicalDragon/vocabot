package net.medrag.vocabot.model.exceptions

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
class PermissionsViolationException(override val message: String) : RuntimeException(message)
