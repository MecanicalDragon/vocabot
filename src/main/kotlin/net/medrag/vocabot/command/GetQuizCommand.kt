package net.medrag.vocabot.command

import net.medrag.vocabot.bot.idString
import net.medrag.vocabot.service.QuizService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Component
class GetQuizCommand(
    private val quizService: QuizService
) : AbstractCommand(
    "quiz",
    "Get a quiz to check yourself"
) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        quizService.createQuiz(chat.idString()).onEach {
            absSender?.execute(it)
        }
    }
}
