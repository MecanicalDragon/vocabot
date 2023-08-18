package net.medrag.vocabot.service

import net.medrag.vocabot.bot.*
import net.medrag.vocabot.dao.SubscriptionRepository
import net.medrag.vocabot.model.WordPairWithId
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
    private val vocabularyService: VocabularyService,
    private val subscriptionRepository: SubscriptionRepository
) {

    fun getWordsToCheck(chat: String, number: Int): List<SendMessage> {
        val chatLong = chat.toLong()
        var words = vocabularyService.getToCheck(chatLong, number)
        if (words.isEmpty()) {
            subscriptionRepository.setWordIndexForSubscription(chatLong, 1)
            words = vocabularyService.getToCheck(chatLong, number)
        } else {
            subscriptionRepository.incrementWordIndexForSubscription(chatLong, number)
        }
        val messages = ArrayList<SendMessage>(words.size + 1)
        for (word in words) {
            SendMessage().apply {
                parseMode = "HTML"
                chatId = chat
                text = word.toMaskedText()
            }.also {
                it.replyMarkup = addToLearnMarkup(word.id)
                messages.add(it)
            }
        }
        messages.add(buildCheckMarkup(chat, number))
        return messages
    }

    fun getWordsToLearn(chat: String, number: Int): List<SendMessage> {
        val wordIds = subscriptionRepository.getToLearn(chat.toLong(), number)
        val words = vocabularyService.getToLearn(wordIds.map { it.toInt() }.shuffled())
        val messages = ArrayList<SendMessage>(words.size + 1)
        for (word in words) {
            SendMessage().apply {
                parseMode = "HTML"
                chatId = chat
                text = word.toMaskedText()
            }.also {
                it.replyMarkup = addLearnedMarkup(word.id)
                messages.add(it)
            }
        }
        messages.add(buildLearnMarkup(chat, number))
        return messages
    }

    private fun buildLearnMarkup(chat: String, number: Int) = SendMessage().apply {
        chatId = chat
        text = "Learn other $number?"
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

    private fun buildCheckMarkup(chat: String, number: Int) = SendMessage().apply {
        chatId = chat
        text = "Check other $number?"
        replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(
            listOf(
                InlineKeyboardButton.builder()
                    .text("Yes")
                    .callbackData(CALLBACK_PREFIX_GET_CHECK + CALLBACK_DELIMITER + number)
                    .build(),
                InlineKeyboardButton.builder()
                    .text("No")
                    .callbackData(CALLBACK_PREFIX_GET_CHECK + CALLBACK_DELIMITER + 0)
                    .build()
            )
        ).build()
    }

    private fun WordPairWithId.toMaskedText(): String {
        return this.word2 + "\n\n" + spoiler(
            bold(this.word1) + "\n\n" + this.examples.toNewLinedString()
        )
    }
}
