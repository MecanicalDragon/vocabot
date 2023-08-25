package net.medrag.vocabot.service.scheduled

import mu.KotlinLogging
import net.medrag.vocabot.config.MasterProps
import net.medrag.vocabot.service.CommandExecutor
import net.medrag.vocabot.service.QuizService
import net.medrag.vocabot.service.SubscriptionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ForwardMessage
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

/**
 * @author Stanislav Tretiakov
 * 24.08.2023
 */
@Service
class ScheduledQuizService(
    private val subscriptionService: SubscriptionService,
    private val quizService: QuizService,
    private val masterProps: MasterProps,
    private val commandExecutor: CommandExecutor
) {

    @Scheduled(cron = "\${net.medrag.vocabot.post-quiz-cron}")
    fun sendQuiz() {
        logger.info { "Batch of words is going to be sent as a quiz." }
        val quiz = quizService.createQuizCommon(masterProps.sourceChat)
        val msg: Message = commandExecutor.send(quiz[0])
        subscriptionService.getSubscriptions().forEach {
//            if (it.subId.toString() == "323207846") {
            try {
                commandExecutor.send(
                    ForwardMessage().apply {
                        chatId = it.subId.toString()
                        fromChatId = msg.chatId.toString()
                        messageId = msg.messageId
                    }
                )
                val addMessage = quiz[1] as SendMessage
                commandExecutor.send(
                    SendMessage().apply {
                        chatId = it.subId.toString()
                        text = "Add to learning list?"
                        disableNotification = true
                        replyMarkup = addMessage.replyMarkup
                    }
                )
            } catch (e: Exception) {
                logger.warn(e) { "Exception occurred sending a poll to the chat <${it.subId}>." }
            }
//            }
        }
    }
}

private val logger = KotlinLogging.logger { }
