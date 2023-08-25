package net.medrag.vocabot.bot

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import net.medrag.vocabot.callback.ExecutionWay
import net.medrag.vocabot.command.AbstractCommand
import net.medrag.vocabot.config.MasterProps
import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.model.events.*
import net.medrag.vocabot.model.exceptions.PermissionsViolationException
import net.medrag.vocabot.modes.Mode
import net.medrag.vocabot.modes.ModeName
import net.medrag.vocabot.service.CommandExecutor
import net.medrag.vocabot.service.ServiceFacade
import net.medrag.vocabot.service.admin.AdminCommandExecutor
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.ForwardMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Stanislav Tretyakov
 * 17.04.2022
 */
@Service
class VocaBot(
    private val commands: List<AbstractCommand>,
    private val masterProps: MasterProps,
    private val serviceFacade: ServiceFacade,
    _modes: List<Mode>,
    _adminCommands: List<AdminCommandExecutor>
) : TelegramLongPollingCommandBot(), CommandExecutor {

    private val modes = _modes.associateBy { it.name() }
    private val adminCommands = _adminCommands.associateBy { it.command() }

    private var currentMode: Mode = modes[ModeName.DEFAULT]
        ?: throw IllegalStateException("Mode <${ModeName.DEFAULT}> couldn't be found.")

    @PostConstruct
    fun init() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        logger.info("VocaBot has been registered.")
        for (command in commands) {
            register(command)
        }
    }

    @EventListener(AdministrationEvent::class)
    fun handleAdminEvent(event: AdministrationEvent) {
        try {
            checkCommander(event.commanderInfo)
            applyAdminEvent(event.commanderInfo)
            logger.info { "Admin command has been applied successfully." }
        } catch (e: PermissionsViolationException) {
            SendMessage().apply {
                chatId = event.commanderInfo.chat.idString()
                text = e.message
            }.also {
                execute(it)
            }
        }
    }

    @EventListener(SwitchModeEvent::class)
    fun switchMode(event: SwitchModeEvent) {
        checkCommander(event.commanderInfo)
        setMode(event.mode)
    }

    @EventListener(PostEvent::class)
    fun postIdiom(event: PostEvent) {
        val msg: Message = execute(
            SendMessage().apply {
                chatId = masterProps.sourceChat
                text = event.message
                if (event.html) {
                    parseMode = "HTML"
                }
            }
        )
        serviceFacade.getSubscriptions().forEach {
            executeAsync(
                ForwardMessage().apply {
                    chatId = it.subId.toString()
                    fromChatId = msg.chatId.toString()
                    messageId = msg.messageId
                }
            )
        }
    }

    @EventListener(NextPersonalQuizEvent::class)
    fun processNextQuizCallback(event: NextPersonalQuizEvent) {
        executeAsync(AnswerCallbackQuery(event.update.callbackQuery.id))
        executeAsync(
            DeleteMessage().apply {
                messageId = event.update.callbackQuery.message.messageId
                chatId = event.update.callbackQuery.message.chatId.toString()
            }
        )
        if (event.answer === NextPersonalQuizEvent.Answer.YES) {
            serviceFacade.createQuiz(event.update.chatIdFromCallback()).onEach {
                executeAsync(it)
            }
        }
    }

    private fun checkCommander(commanderInfo: CommanderInfo) {
        logger.info { "User <${commanderInfo.user?.userName}> invoked an admin command." }
        if (commanderInfo.user?.userName != masterProps.master) {
            throw PermissionsViolationException("You're not able to invoke this command.")
        }
    }

    private fun applyAdminEvent(commanderInfo: CommanderInfo) {
        logger.info {
            "Admin command has been received: sender: <${commanderInfo.user?.userName}>, " +
                "chat id: <${commanderInfo.chat.idString()}>, " +
                "command arguments: [${commanderInfo.arguments.argsToString()}]."
        }
        commanderInfo.arguments?.get(0)?.let {
            adminCommands[it]?.execute(commanderInfo)
        }
    }

    private fun setMode(name: ModeName) {
        currentMode = modes[name] ?: throw IllegalStateException("Mode <$name> couldn't be found.")
        logger.info { "Mode <$name> has been set." }
    }

    override fun <T : Serializable?, Method : BotApiMethod<T>?> sendAsync(method: Method?): CompletableFuture<T>? = executeAsync(method)

    override fun <T : Serializable?, Method : BotApiMethod<T>?> send(method: Method?): T = execute(method)

    override fun getBotToken(): String = masterProps.token

    override fun getBotUsername(): String = masterProps.botName

    override fun processNonCommandUpdate(update: Update?) {
        currentMode.processNonCommandUpdate(update)?.let {
            for (replyAction in it.replyActions) {
                when (replyAction.executionWay) {
                    ExecutionWay.DEFAULT -> execute(replyAction.botApiMethod)
                    ExecutionWay.ASYNC -> executeAsync(replyAction.botApiMethod)
                }
            }
        }
    }
}

private val logger = KotlinLogging.logger { }
