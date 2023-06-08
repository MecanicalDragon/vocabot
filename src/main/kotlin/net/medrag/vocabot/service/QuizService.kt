package net.medrag.vocabot.service

import net.medrag.vocabot.bot.toNewLinedString
import net.medrag.vocabot.model.WordPairDto
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class QuizService {

    fun createQuiz(words: List<WordPairDto>): SendPoll {
        val correct = (Math.random() * words.size).toInt()
        return SendPoll().apply {
            question = words[correct].word2
            correctOptionId = correct
            type = "quiz"
            isAnonymous = false
            protectContent = false
            explanation = explanation(words[correct])
            options = words.map {
                it.word1
            }.toList()
        }
    }

    private fun explanation(word: WordPairDto): String? {
        return if (word.examples.isNotEmpty()) {
            word.examples.toNewLinedString()
        } else {
            null
        }
    }
}
