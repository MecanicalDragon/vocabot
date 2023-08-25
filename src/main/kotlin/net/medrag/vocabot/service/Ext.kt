package net.medrag.vocabot.service

import net.medrag.vocabot.bot.CALLBACK_PREFIX_ADD_TO_LEARN
import net.medrag.vocabot.bot.CALLBACK_PREFIX_REMOVE_FROM_LEARN
import net.medrag.vocabot.bot.callback
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

/**
 * @author Stanislav Tretiakov
 * 18.08.2023
 */
fun addToLearnMarkup(wordId: Int): InlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(
    listOf(
        InlineKeyboardButton.builder()
            .text("Add to learning")
            .callback(CALLBACK_PREFIX_ADD_TO_LEARN, wordId)
            .build()
    )
).build()

fun addLearnedMarkup(wordId: Int): InlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(
    listOf(
        InlineKeyboardButton.builder()
            .text("Learned")
            .callback(CALLBACK_PREFIX_REMOVE_FROM_LEARN, wordId)
            .build()
    )
).build()
