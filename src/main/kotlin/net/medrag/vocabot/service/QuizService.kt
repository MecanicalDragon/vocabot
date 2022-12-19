package net.medrag.vocabot.service

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
        return if (word.examples.isEmpty())
            null
//            "No examples are presented. You can add some with '/ex' command ('/help' for thorough instruction)."
        else word.examples.toNewLinedString()
    }

    private fun List<String>.toNewLinedString(): String = asSequence().joinToString(separator = "\n")
}
