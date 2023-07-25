package net.medrag.vocabot.service

import net.medrag.vocabot.dao.WordPair
import net.medrag.vocabot.dao.WordPairRepository
import net.medrag.vocabot.model.WordPairDto
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class ServiceFacade(
    private val wordPairRepository: WordPairRepository,
    private val subscriptionService: SubscriptionService,
    private val checkWordsService: CheckWordsService,
    private val vocabularyService: VocabularyService,
    private val quizService: QuizService
) {
    fun saveAll(words: List<WordPair>) {
        wordPairRepository.saveAll(words)
    }

    fun getSubscriptions() = subscriptionService.getSubscriptions()

    fun createQuiz(words: List<WordPairDto>): SendPoll = quizService.createQuiz(words)

    fun createQuiz(): SendPoll = with(vocabularyService.getSome()) {
        quizService.createQuiz(this)
    }

    fun learn(number: Int, chatId: String) = checkWordsService.getWordsToCheck(number, chatId)
}
