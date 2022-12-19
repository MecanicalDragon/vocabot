package net.medrag.vocabot.bot

import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */

fun Chat?.idString() = this?.id?.toString() ?: "0"

fun Update?.callbackPrefix() = this?.callbackQuery?.data?.split(CALLBACK_DELIMITER)?.get(0) ?: INVALID_CALLBACK_PREFIX

fun Update?.chatIdFromCallback() = this?.callbackQuery?.message?.chatId?.toString() ?: "0"

fun Update?.chatIdFromMessage() = this?.message?.chatId?.toString() ?: "0"

fun Update?.messageIdFromCallback() = this?.callbackQuery?.message?.messageId ?: 0

fun Update?.userFromCallback() = this?.callbackQuery?.from

fun Array<out String>?.argsToString(skip: Int = 0): String {
    if (this == null) return "null"
    val iMax = size - 1
    if (iMax == -1) return "empty args"
    val b = StringBuilder()
    var i = skip
    while (true) {
        b.append(this[i])
        if (i++ == iMax) return b.toString()
        b.append(" ")
    }
}
