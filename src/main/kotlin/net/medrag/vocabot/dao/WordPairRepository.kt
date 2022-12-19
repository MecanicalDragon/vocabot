package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface WordPairRepository : JpaRepository<WordPair, Int> {

    @Query(nativeQuery = true, value = "SELECT * FROM vocabulary ORDER BY random() LIMIT 1")
    fun findRandom(): WordPair

    @Query(nativeQuery = true, value = "SELECT * FROM vocabulary ORDER BY random() LIMIT :l")
    fun findSome(@Param(value = "l") l: Int): List<WordPair>

    fun findByLang1(lang1: String): WordPair?

    @Query(value = "from WordPair v WHERE v.lang1 like %:l%")
    fun findLike(@Param(value = "l") lang1: String): List<WordPair>
}
