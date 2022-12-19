package net.medrag.vocabot.service.admin

import net.medrag.vocabot.model.CommanderInfo

/**
 * @author Stanislav Tretyakov
 * 22.04.2022
 */
interface AdminCommandExecutor {
    fun command(): String
    fun execute(commanderInfo: CommanderInfo)
}
