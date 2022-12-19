package net.medrag.vocabot.command

import net.medrag.vocabot.bot.CALLBACK_DELIMITER
import net.medrag.vocabot.bot.CALLBACK_PREFIX_GET_QUIZ
import net.medrag.vocabot.bot.idString
import net.medrag.vocabot.model.events.NextPersonalQuizEvent
import net.medrag.vocabot.service.QuizService
import net.medrag.vocabot.service.VocabularyService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class GetWordCommand(
    private val vocabularyService: VocabularyService,
    private val quizService: QuizService,
) : AbstractCommand(
    "get",
    "Get word from vocabulary to check yourself"
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val words = vocabularyService.getSome()
        quizService.createQuiz(words).also {
            it.chatId = chat.idString()
            absSender?.execute(it)
        }
        SendMessage().apply {
            chatId = chat.idString()
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
        }.also {
            absSender?.execute(it)
        }
    }
}
