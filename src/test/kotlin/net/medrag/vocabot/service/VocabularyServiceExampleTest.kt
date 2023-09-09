package net.medrag.vocabot.service

import net.medrag.vocabot.config.VocProps
import net.medrag.vocabot.dao.WordPair
import net.medrag.vocabot.dao.WordPairRepository
import net.medrag.vocabot.model.CommanderInfo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.check
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockitoExtension::class)
@Suppress("unused")
internal class VocabularyServiceExampleTest {

    private val delimiter: String = "."

    @Mock
    private lateinit var wordPairRepository: WordPairRepository

    @Mock
    private lateinit var subscriptionService: SubscriptionService

    @Mock
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Mock
    private lateinit var vocProps: VocProps

    @InjectMocks
    private lateinit var vocabularyService: VocabularyService

    @Test
    fun `must add new example for word`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret", delimiter, "this", "is", "example"))
        val word = WordPair(123, "ferret", "хорёк")
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(1, it.examples.size) },
                    { assertEquals("this is example", it.examples[0]) }
                )
            }
        )
    }

    @Test
    fun `must handle delimiter in the end of the word`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret$delimiter", "this", "is", "example"))
        val word = WordPair(123, "ferret", "хорёк")
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(1, it.examples.size) },
                    { assertEquals("this is example", it.examples[0]) }
                )
            }
        )
    }

    @Test
    fun `must handle delimiters in the end of the word and example`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret$delimiter", "this", "is", "example$delimiter"))
        val word = WordPair(123, "ferret", "хорёк")
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(1, it.examples.size) },
                    { assertEquals("this is example.", it.examples[0]) }
                )
            }
        )
    }

    @Test
    fun `must handle delimiters in the end of the word and example again`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret$delimiter", "this", "is", "example$delimiter", delimiter))
        val word = WordPair(123, "ferret", "хорёк")
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(1, it.examples.size) },
                    { assertEquals("this is example.", it.examples[0]) }
                )
            }
        )
    }

    @Test
    fun `must add another example for word`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret", delimiter, "this", "is", "example"))
        val word = WordPair(123, "ferret", "хорёк", listOf("another example"))
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(2, it.examples.size) },
                    { assertEquals("another example", it.examples[0]) },
                    { assertEquals("this is example", it.examples[1]) }
                )
            }
        )
    }

    @Test
    fun `must add two examples for word`() {
        val commanderInfo = CommanderInfo(arguments = arrayOf("ferret", delimiter, "this", "is", "example", delimiter, "example", "three"))
        val word = WordPair(123, "ferret", "хорёк", listOf("another example"))
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(3, it.examples.size) },
                    { assertEquals("another example", it.examples[0]) },
                    { assertEquals("this is example", it.examples[1]) },
                    { assertEquals("example three", it.examples[2]) }
                )
            }
        )
    }

    @Test
    fun `must add two examples for word 2`() {
        val commanderInfo =
            CommanderInfo(arguments = arrayOf("ferret", delimiter, "this", "is", "example$delimiter", "example", "three", delimiter))
        val word = WordPair(123, "ferret", "хорёк", listOf("another example"))
        whenever(vocProps.delimiter).thenReturn(delimiter)
        whenever(wordPairRepository.findByLang1("ferret")).thenReturn(word)
        whenever(wordPairRepository.save(word)).thenReturn(word)

        vocabularyService.addExample(commanderInfo)

        verify(wordPairRepository).save(
            check {
                assertAll(
                    { assertEquals("ferret", it.lang1) },
                    { assertEquals("хорёк", it.lang2) },
                    { assertNotNull(it.examples) },
                    { assertEquals(3, it.examples.size) },
                    { assertEquals("another example", it.examples[0]) },
                    { assertEquals("this is example.", it.examples[1]) },
                    { assertEquals("example three", it.examples[2]) }
                )
            }
        )
    }
}
