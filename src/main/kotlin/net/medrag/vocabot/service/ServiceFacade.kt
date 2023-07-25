package net.medrag.vocabot.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class ServiceFacade(
    private val subscriptionService: SubscriptionService,
    private val checkWordsService: CheckWordsService,
    private val quizService: QuizService
) {

    fun getSubscriptions() = subscriptionService.getSubscriptions()

    fun createQuiz(): SendPoll = quizService.createQuiz()

    fun learn(number: Int, chatId: String) = checkWordsService.getWordsToCheck(number, chatId)
}
