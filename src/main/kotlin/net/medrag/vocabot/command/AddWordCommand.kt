package net.medrag.vocabot.command

import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.model.exceptions.InputFormatException
import net.medrag.vocabot.model.exceptions.WordAlreadyExistsException
import net.medrag.vocabot.service.VocabularyService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class AddWordCommand(
    private val vocabularyService: VocabularyService
) : AbstractCommand(
    "add",
    """
        Add new word to vocabulary. Input must conform pattern <words(s) of 1 language> - <words(s) of 2 language> 
        <optionally: '. ', followed by set of examples, separated with '. '>.
        """.trimIndent()
) {
    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        try {
            val word = vocabularyService.addWord(CommanderInfo(absSender, user, chat, arguments))
            absSender?.execute(
                SendMessage().apply {
                    text = "New word has been added:\n${word.word1} - ${word.word2}" +
                            if (word.examples.isEmpty()) "." else ": ${word.examples.joinToString(separator = " ")}"
                    chatId = chat?.id.toString()
                }
            )
        } catch (e: InputFormatException) {
            absSender?.execute(
                SendMessage().apply {
                    text = e.message ?: "Looks like you've input words in some unsupported format."
                    chatId = chat?.id.toString()
                }
            )
        } catch (e: WordAlreadyExistsException) {
            absSender?.execute(
                SendMessage().apply {
                    text = e.message
                    chatId = chat?.id.toString()
                }
            )
        }
    }
}
