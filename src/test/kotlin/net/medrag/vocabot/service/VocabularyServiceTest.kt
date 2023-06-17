package net.medrag.vocabot.service

import net.medrag.vocabot.config.VocProps
import net.medrag.vocabot.dao.WordPair
import net.medrag.vocabot.dao.WordPairRepository
import net.medrag.vocabot.model.exceptions.InputFormatException
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.ApplicationEventPublisher
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

@ExtendWith(MockitoExtension::class)
@Suppress("unused")
internal class VocabularyServiceTest {

    private val delimiter: String = "."

    @Mock
    private lateinit var wordPairRepository: WordPairRepository

    @Mock
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Mock
    private lateinit var vocProps: VocProps

    @InjectMocks
    private lateinit var vocabularyService: VocabularyService

    @Test
    fun `must build valid word pair when first word is eng`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("dog", "-", "пёс")) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("dog", wordPair?.lang1) },
            { assertEquals("пёс", wordPair?.lang2) }
        )
    }

    @Test
    fun `must build valid word pair when first word is rus`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("пёс", "-", "dog")) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("dog", wordPair?.lang1) },
            { assertEquals("пёс", wordPair?.lang2) }
        )
    }

    @Test
    fun `must fail because of language mix 1`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> { buildPair?.call(vocabularyService, arrayOf("пёс", "-", "dogя")) }
        println(e)
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must fail because of language mix 2`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> { buildPair?.call(vocabularyService, arrayOf("пёсi", "-", "dog")) }
        println(e)
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must fail because of language mix 3`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> { buildPair?.call(vocabularyService, arrayOf("dog", "-", "пёсi")) }
        println(e)
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must fail because of language mix 4`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> { buildPair?.call(vocabularyService, arrayOf("dogя", "-", "пёс")) }
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must trim spaces`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("dog   ", "-", "   пёс")) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("dog", wordPair?.lang1) },
            { assertEquals("пёс", wordPair?.lang2) }
        )
    }

    @Test
    fun `must combine words`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("ferret", "-", "выискивать,", "хорёк")) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) }
        )
    }

    @Test
    fun `must trim delimiter`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("ferret", "-", "выискивать,", "хорёк", delimiter)) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(0, wordPair?.examples?.size) }
        )
    }

    @Test
    fun `must trim delimiters`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("ferret", "-", "выискивать,", "хорёк", delimiter, delimiter)) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(0, wordPair?.examples?.size) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the word 1`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter")) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(0, wordPair?.examples?.size) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the word 2`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(vocabularyService, arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter", delimiter)) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(0, wordPair?.examples?.size) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the word 3`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter", "this", "is", "example.", delimiter)
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the word 4`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter", "this", "is", "example..", delimiter)
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example..", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 1`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter", "this", "is", "example$delimiter")
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 2`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк$delimiter", "this", "is", "example$delimiter", delimiter)
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 3`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк$delimiter",
                "this", "is", "example$delimiter", delimiter, "this", "is", "another", "example"
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example", wordPair?.examples?.get(1)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 4`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк$delimiter",
                "this", "is", "example$delimiter", "this", "is", "another", "example$delimiter"
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example.", wordPair?.examples?.get(1)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 5`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк", delimiter,
                "this", "is", "example$delimiter", "this", "is", "another", "example", delimiter
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example.", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example", wordPair?.examples?.get(1)) }
        )
    }

    @Test
    fun `must recognize delimiter in the end of the example 6`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк", delimiter,
                "this", "is", "example", delimiter, "this", "is", "another", "example$delimiter"
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example.", wordPair?.examples?.get(1)) }
        )
    }

    @Test
    fun `must throw exception because of doubled delimiter`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> {
            buildPair?.call(
                vocabularyService,
                arrayOf("ferret", "-", "выискивать,", "хорёк", delimiter + delimiter)
            )
        }
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must add example`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк", delimiter, "this", "is", "example:")
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example:", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must not add example because of forbidden symbol in word`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val e = assertThrows<InvocationTargetException> {
            buildPair?.call(
                vocabularyService,
                arrayOf("ferret:", "-", "выискивать,", "хорёк", delimiter, "this", "is", "example")
            )
        }
        assertAll(
            "built word pair",
            { assertEquals(InputFormatException::class.java, e.cause?.javaClass) }
        )
    }

    @Test
    fun `must add exactly one example`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf("ferret", "-", "выискивать,", "хорёк", delimiter, delimiter, "this", "is", "example")
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(1, wordPair?.examples?.size) },
            { assertEquals("this is example", wordPair?.examples?.get(0)) }
        )
    }

    @Test
    fun `must add several examples 1`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк", delimiter, delimiter, "this", "is", "example",
                delimiter, delimiter, "this", "is", "another", "example"
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example", wordPair?.examples?.get(1)) }
        )
    }

    @Test
    fun `must add several examples 2`() {
        `when`(vocProps.delimiter).thenReturn(delimiter)
        val buildPair = getBuildPairFunction()
        val wordPair = buildPair?.call(
            vocabularyService,
            arrayOf(
                "ferret", "-", "выискивать,", "хорёк", delimiter, "this", "is", "example",
                delimiter, "this", "is", "another", "example", delimiter
            )
        ) as? WordPair
        assertAll(
            "built word pair",
            { assertEquals("ferret", wordPair?.lang1) },
            { assertEquals("выискивать, хорёк", wordPair?.lang2) },
            { assertEquals(2, wordPair?.examples?.size) },
            { assertEquals("this is example", wordPair?.examples?.get(0)) },
            { assertEquals("this is another example", wordPair?.examples?.get(1)) }
        )
    }

    private fun getBuildPairFunction(): KFunction<*>? {
        val buildPair = vocabularyService::class.declaredFunctions.find { it.name == "buildPair" }
        buildPair?.isAccessible = true
        return buildPair
    }
}
