package net.medrag.vocabot.service

import net.medrag.vocabot.bot.*
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
    fun createQuizPersonal(chatId: String): List<BotApiMethodMessage> = listOf(getThePoll(chatId).first, getTheProceedQuestion(chatId))
    fun createQuizCommon(chatId: String): List<BotApiMethodMessage> = with(getThePoll(chatId)) {
        listOf(first, getTheLearningQuestion(chatId, second.id))
    }

    private fun getThePoll(chat: String): Pair<SendPoll, WordPairWithId> {
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
        } to correctWord
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

    private fun getTheLearningQuestion(chat: String, wordId: Int): SendMessage =
        SendMessage().apply {
            chatId = chat
            text = "Add to learning list?"
            disableNotification = true
            replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(
                listOf(
                    InlineKeyboardButton.builder()
                        .text("Add to learning")
                        .callback(CALLBACK_PREFIX_ADD_TO_LEARN_QUIZ, wordId)
                        .build()
                )
            ).build()
        }
}
