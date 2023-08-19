package net.medrag.vocabot.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class ServiceFacade(
    private val subscriptionService: SubscriptionService,
    private val quizService: QuizService
) {

    fun getSubscriptions() = subscriptionService.getSubscriptions()

    fun createQuiz(chatId: String): List<BotApiMethodMessage> = quizService.createQuiz(chatId)
}
