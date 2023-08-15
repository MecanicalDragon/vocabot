package net.medrag.vocabot.service

import net.medrag.vocabot.dao.WordPairRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Stanislav Tretiakov
 * 15.08.2023
 */
@Service
@Transactional
class LearningService(
    private val wordPairRepository: WordPairRepository
) {
    fun learned(chatId: String, wordId: Int) {
        wordPairRepository.learnWord(0, wordId)
    }

    fun toLearn(chatId: String, wordId: Int) {
        wordPairRepository.learnWord(1, wordId)
    }
}
