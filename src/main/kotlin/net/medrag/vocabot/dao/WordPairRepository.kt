package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface WordPairRepository : JpaRepository<WordPair, Int> {

    @Query(nativeQuery = true, value = "SELECT * FROM vocabulary ORDER BY RAND() LIMIT 1")
    fun findRandom(): WordPair

    @Query(nativeQuery = true, value = "SELECT * FROM vocabulary ORDER BY RAND() LIMIT :l")
    fun findSome(@Param(value = "l") l: Int): List<WordPair>

    @Query(nativeQuery = true, value = "SELECT * FROM vocabulary WHERE id IN :ids")
    fun findToLearn(@Param(value = "ids") ids: List<Int>): List<WordPair>

    @Query(
        nativeQuery = true,
        value = "SELECT * FROM vocabulary WHERE id >= (SELECT last_word_id FROM subscriptions WHERE subscription_id = :n) ORDER BY id LIMIT :l"
    )
    fun getToCheck(@Param(value = "n") n: Long, @Param(value = "l") l: Int): List<WordPair>

    fun findByLang1(lang1: String): WordPair?

    @Query(value = "from WordPair v WHERE v.lang1 like %:l%")
    fun findLike(@Param(value = "l") lang1: String): List<WordPair>

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE vocabulary SET value = :value WHERE id = :id")
    fun learnWord(@Param(value = "value") value: Int, @Param(value = "id") id: Int): Int
}
