package net.medrag.vocabot.service

import mu.KotlinLogging
import net.medrag.vocabot.bot.argsToString
import net.medrag.vocabot.config.VocProps
import net.medrag.vocabot.dao.IdiomRepository
import net.medrag.vocabot.dao.IrregularVerbRepository
import net.medrag.vocabot.dao.WordPair
import net.medrag.vocabot.dao.WordPairRepository
import net.medrag.vocabot.model.CommanderInfo
import net.medrag.vocabot.model.IdiomDto
import net.medrag.vocabot.model.IrregularVerbDto
import net.medrag.vocabot.model.WordPairDto
import net.medrag.vocabot.model.events.PostIdiomEvent
import net.medrag.vocabot.model.events.PostIrregularVerbEvent
import net.medrag.vocabot.model.events.PostQuizEvent
import net.medrag.vocabot.model.exceptions.InputFormatException
import net.medrag.vocabot.model.exceptions.WordAlreadyExistsException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Service
class VocabularyService(
    private val wordRepo: WordPairRepository,
    private val irregularRepo: IrregularVerbRepository,
    private val idiomRepo: IdiomRepository,
    private val publisher: ApplicationEventPublisher,
    private val vocProps: VocProps
) {

    @Scheduled(cron = "\${net.medrag.vocabot.post-word-cron}")
    fun sendIrregularVerb() {
        val random = Math.random()
        if (random > 0.5) {
            logger.info { "Irregular verb is going to be sent." }
            val irregular = irregularRepo.findRandom()
            publisher.publishEvent(PostIrregularVerbEvent(IrregularVerbDto(irregular.form1, irregular.form2, irregular.form3), this))
        } else {
            logger.info { "Idiom is going to be sent." }
            val idiom = idiomRepo.findRandom()
            publisher.publishEvent(PostIdiomEvent(IdiomDto(idiom.idiom, idiom.meaning, idiom.examples), this))
        }
    }

    @Scheduled(cron = "\${net.medrag.vocabot.post-quiz-cron}")
    fun sendQuiz() {
        logger.info { "Batch of words is going to be sent as a quiz." }
        val list = wordRepo.findSome(vocProps.wordsInQuiz).map { WordPairDto(it.lang1, it.lang2, it.examples) }.toList()
        publisher.publishEvent(PostQuizEvent(list, this))
    }

    fun getSome(): List<WordPairDto> {
        return wordRepo.findSome(vocProps.wordsInQuiz).map { WordPairDto(it.lang1, it.lang2, it.examples) }.toList()
    }

    fun addWord(commanderInfo: CommanderInfo): WordPairDto {
        logger.info { "User <${commanderInfo.user?.userName}> adds new word pair: <${commanderInfo.arguments.argsToString()}>." }
        val newPair: WordPair = buildPair(commanderInfo.arguments)
        try {
            wordRepo.save(newPair)
        } catch (e: DataIntegrityViolationException) {
            throw WordAlreadyExistsException(
                "Looks like word <${WordPairDto(newPair.lang1, newPair.lang2)}> already exists in vocabulary."
            )
        }
        logger.info { "New word pair has been saved: <$newPair>." }
        return WordPairDto(newPair.lang1, newPair.lang2, newPair.examples)
    }

    @Transactional
    fun addExample(commanderInfo: CommanderInfo): WordPairDto {
        logger.info { "User <${commanderInfo.user?.userName}> adds new example for word: <${commanderInfo.arguments.argsToString()}>." }
        val wordBuilder = StringBuilder()
        val exampleBuilder = StringBuilder()
        val examplesList = mutableListOf<String>()
        var wordSeparator = false
        commanderInfo.arguments?.let { args ->
            for (arg in args) {
                if (wordSeparator) {
                    if (arg == vocProps.delimiter) {
                        examplesList.add(exampleBuilder.toString().trim())
                        exampleBuilder.clear()
                    } else if (arg.last().toString() == vocProps.delimiter) {
                        checkSequenceForExampleCompliance(arg)
                        examplesList.add(exampleBuilder.append(arg).toString().trim())
                        exampleBuilder.clear()
                    } else {
                        checkSequenceForExampleCompliance(arg)
                        exampleBuilder.append(arg).append(" ")
                    }
                } else {
                    if (arg == vocProps.delimiter) {
                        wordSeparator = true
                    } else if (arg.last().toString() == vocProps.delimiter) {
                        wordBuilder.append(arg.dropLast(1))
                        wordSeparator = true
                    } else {
                        wordBuilder.append(arg).append(" ")
                    }
                }
            }
            if (exampleBuilder.isNotBlank()) {
                examplesList.add(exampleBuilder.toString().trim())
                exampleBuilder.clear()
            }
        }
        val word = wordBuilder.trim().toString()
        if (vocProps.useLike) {
            wordRepo.findLike(word).let {
                if (it.isEmpty()) {
                    throw InputFormatException("Pattern <$word> has not been found in vocabulary!")
                } else if (it.size > 1) {
                    throw InputFormatException("Pattern <$word> is not precise enough!")
                } else {
                    with(it[0]) {
                        examplesList.filter { it.isNotBlank() }.forEach {
                            if (it.isNotEmpty()) {
                                this.examples += it
                            }
                        }
                    }
                    val saved = wordRepo.save(it[0])
                    return WordPairDto(saved.lang1, saved.lang2, saved.examples)
                }
            }
        } else {
            wordRepo.findByLang1(word)?.let {
                with(examplesList.filter { it.isNotBlank() }) {
                    if (isNotEmpty()) {
                        it.examples += this
                    }
                }
                val saved = wordRepo.save(it)
                return WordPairDto(saved.lang1, saved.lang2, saved.examples)
            } ?: throw InputFormatException("Word <$word> has not been found in vocabulary!")
        }
    }

    private fun buildPair(args: Array<out String>?) = args?.let {
        val word1Sb = StringBuilder()
        val word2Sb = StringBuilder()
        val example = StringBuilder()
        val examples = mutableListOf<String>()
        var hyphen = false
        var delimiterMet = false
        for (singleArg in it) {
            if (singleArg == "-") {
                hyphen = true
            } else if (hyphen) {
                if (singleArg == vocProps.delimiter) {
                    delimiterMet = true
                    addExample(example, examples)
                } else if (singleArg.last().toString() == vocProps.delimiter) {
                    if (delimiterMet) {
                        example.append(singleArg).append(" ")
                        addExample(example, examples)
                    } else {
                        delimiterMet = true
                        word2Sb.append(singleArg.dropLast(1))
                    }
                } else if (delimiterMet) {
                    example.append(singleArg).append(" ")
                } else {
                    word2Sb.append(singleArg).append(" ")
                }
            } else {
                word1Sb.append(singleArg).append(" ")
            }
        }
        addExample(example, examples)
        val word1 = word1Sb.toString().trim()
        val word2 = word2Sb.toString().trim()
        if (word1.isBlank() || word2.isBlank()) throw InputFormatException("Words must not be empty!")
        val lang1: String
        val lang2: String
        if (checkEnglish(word1[0])) {
            checkSequenceForEnglishOnly(word1)
            checkSequenceForRussianOnly(word2)
            lang1 = word1
            lang2 = word2
        } else if (checkRussian(word1[0])) {
            checkSequenceForRussianOnly(word1)
            checkSequenceForEnglishOnly(word2)
            lang1 = word2
            lang2 = word1
        } else {
            throw InputFormatException(ERROR_MSG)
        }

        WordPair(lang1 = lang1, lang2 = lang2, examples = examples)
    } ?: throw InputFormatException("Args array must not be null!")

    private fun addExample(example: StringBuilder, examples: MutableList<String>) {
        if (example.isNotBlank()) {
            checkSequenceForExampleCompliance(example)
            examples.add(example.trim().toString())
            example.clear()
        }
    }

    private fun checkEnglish(c: Char): Boolean = ENG_CHARS.contains(c)

    private fun checkRussian(c: Char): Boolean = RUS_CHARS.contains(c)

    private fun checkExample(c: Char): Boolean = ENG_CHARS.contains(c) || EXAMPLE_CHARS.contains(c)

    private fun checkSequenceForEnglishOnly(sequence: CharSequence) {
        for (c in sequence) {
            if (!checkEnglish(c)) {
                throw InputFormatException(ERROR_MSG)
            }
        }
    }

    private fun checkSequenceForRussianOnly(sequence: CharSequence) {
        for (c in sequence) {
            if (!checkRussian(c)) {
                throw InputFormatException(ERROR_MSG)
            }
        }
    }

    private fun checkSequenceForExampleCompliance(sequence: CharSequence) {
        for (c in sequence) {
            if (!checkExample(c)) {
                throw InputFormatException(EXAMPLE_ERROR_MSG)
            }
        }
    }
}

private val ERROR_MSG = """
    Word has not been added! Available symbols for words are: all eng/rus letters, comma, hyphen, parenthesis, space 
    and apostrophe. Also, you can not mix letters of different languages in one word!
""".trimIndent()

private val EXAMPLE_ERROR_MSG = """
    Word has not been added! Available symbols for examples are: all eng letters, comma, hyphen, parenthesis, space, colon, point,  
    and apostrophe. Also, you can not mix letters of different languages in one word!
""".trimIndent()

private val COMMON_CHARS: Set<Char> = setOf(' ', '-', ',', '(', ')')
private val EXAMPLE_CHARS: Set<Char> = setOf('.', ':')
private val ENG_CHARS: Set<Char> = (('a'..'z') + ('A'..'Z') + '\'' + COMMON_CHARS).toSet()
private val RUS_CHARS: Set<Char> = (('А'..'я') + 'Ё' + 'ё' + COMMON_CHARS).toSet()

private val logger = KotlinLogging.logger { }
