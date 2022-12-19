package net.medrag.vocabot.command

import net.medrag.vocabot.bot.idString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.annotation.PostConstruct

/**
 * @author Stanislav Tretyakov
 * 08.02.2021
 */
@Component
class HelpCommand : AbstractCommand(
    "help",
    "Returns list of commands"
) {
    @Lazy
    @Autowired
    lateinit var commandHelper: CommandHelper

    override fun execute(p0: AbsSender?, p1: User?, p2: Chat?, p3: Array<out String>?) {
        commandHelper.execute(p0, p1, p2, p3)
    }
}

@Component
class StartCommand : AbstractCommand(
    "start",
    "Returns list of commands, like /help does"
) {
    @Lazy
    @Autowired
    lateinit var commandHelper: CommandHelper

    override fun execute(p0: AbsSender?, p1: User?, p2: Chat?, p3: Array<out String>?) {
        commandHelper.execute(p0, p1, p2, p3)
    }
}

@Component
class CommandHelper {

    @Autowired
    lateinit var botCommands: List<AbstractCommand>
    var message: String? = null

    fun execute(p0: AbsSender?, p1: User?, p2: Chat?, p3: Array<out String>?) {

        SendMessage().apply {
            text = message ?: "NOT INITIALIZED YET"
            chatId = p2.idString()
        }.also {
            p0?.execute(it)
        }
    }

    @PostConstruct
    fun init() {
        val sb = StringBuilder("Available commands:")
        for (botCommand in botCommands) {
            sb.append("\n/${botCommand.commandIdentifier} - ${botCommand.description}")
        }
        message = sb.toString()
    }
}
