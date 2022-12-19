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
class AddExampleCommand(
    private val vocabularyService: VocabularyService
) : AbstractCommand(
    "ex",
    "Add an example of word usage. Input must conform pattern <words(s) in English> . <set of examples, separated with ' . '>."
) {
    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        try {
            val word = vocabularyService.addExample(CommanderInfo(absSender, user, chat, arguments))
            absSender?.execute(
                SendMessage().apply {
                    text = "New example has been added for word\n${word.word1}: ${word.examples.joinToString(separator = " ")}"
                    chatId = chat?.id.toString()
                }
            )
        } catch (e: InputFormatException) {
            absSender?.execute(
                SendMessage().apply {
                    text = e.message
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
