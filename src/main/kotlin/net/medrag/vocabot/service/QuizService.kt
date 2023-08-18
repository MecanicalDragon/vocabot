package net.medrag.vocabot.service

import net.medrag.vocabot.bot.CALLBACK_DELIMITER
import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_QUIZ
import net.medrag.vocabot.bot.toNewLinedString
import net.medrag.vocabot.model.WordPairWithId
import net.medrag.vocabot.model.events.NextPersonalQuizEvent
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class QuizService(
    private val vocabularyService: VocabularyService
) {
    fun createQuiz(chatId: String): List<BotApiMethodMessage> = listOf(getThePoll(chatId), getTheProceedQuestion(chatId))

    private fun getThePoll(chat: String): SendPoll {
        val words = vocabularyService.getSome()
        val correct = (Math.random() * words.size).toInt()
        val correctWord = words[correct]
        return SendPoll().apply {
            chatId = chat
            question = correctWord.word2
            correctOptionId = correct
            type = "quiz"
            isAnonymous = false
            protectContent = false
            explanation = explanation(correctWord)
            replyMarkup = addToLearnMarkup(correctWord.id)
            options = words.map {
                it.word1
            }.toList()
        }
    }

    private fun explanation(word: WordPairWithId): String? {
        return if (word.examples.isNotEmpty()) {
            word.examples.toNewLinedString()
        } else {
            null
        }
    }

    private fun getTheProceedQuestion(chat: String): SendMessage =
        SendMessage().apply {
            chatId = chat
            text = "Get another quiz?"
            replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(
                listOf(
                    InlineKeyboardButton.builder()
                        .text("Yes")
                        .callbackData(CALLBACK_PREFIX_GET_QUIZ + CALLBACK_DELIMITER + NextPersonalQuizEvent.Answer.YES)
                        .build(),
                    InlineKeyboardButton.builder()
                        .text("No")
                        .callbackData(CALLBACK_PREFIX_GET_QUIZ + CALLBACK_DELIMITER + NextPersonalQuizEvent.Answer.NO)
                        .build()
                )
            ).build()
        }
}
