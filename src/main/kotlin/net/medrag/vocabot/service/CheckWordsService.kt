package net.medrag.vocabot.service

import net.medrag.vocabot.bot.*
import net.medrag.vocabot.model.WordPairDto
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

/**
 * @author Stanislav Tretiakov
 * 25.07.2023
 */
@Service
class CheckWordsService(
    private val vocabularyService: VocabularyService
) {

    fun getWordsToCheck(number: Int, chat: String): List<SendMessage> {
        val words = vocabularyService.getToLearn(number)
        val messages = ArrayList<SendMessage>(words.size + 1)
        for (word in words) {
            SendMessage().apply {
                parseMode = "HTML"
                chatId = chat
                text = word.toMaskedText()
            }.also {
                messages.add(it)
            }
        }
        messages.add(buildMarkup(chat, number))
        return messages
    }

    private fun buildMarkup(chat: String, number: Int) = SendMessage().apply {
        chatId = chat
        text = "Get another $number?"
        replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(
            listOf(
                InlineKeyboardButton.builder()
                    .text("Yes")
                    .callbackData(CALLBACK_PREFIX_GET_LEARN + CALLBACK_DELIMITER + number)
                    .build(),
                InlineKeyboardButton.builder()
                    .text("No")
                    .callbackData(CALLBACK_PREFIX_GET_LEARN + CALLBACK_DELIMITER + 0)
                    .build()
            )
        ).build()
    }

    private fun WordPairDto.toMaskedText(): String {
        return this.word2 + "\n\n" + spoiler(
            bold(this.word1) + "\n\n" + this.examples.toNewLinedString()
        )
    }
}
