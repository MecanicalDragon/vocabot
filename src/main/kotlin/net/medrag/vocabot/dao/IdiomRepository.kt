package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Stanislav Tretyakov
 * 19.12.2022
 */
interface IdiomRepository : JpaRepository<Idiom, Int> {

    @Query(nativeQuery = true, value = "SELECT * FROM idioms ORDER BY random() LIMIT 1")
    fun findRandom(): Idiom

    @Query(nativeQuery = true, value = "SELECT *  FROM idioms ORDER BY random() LIMIT :lim")
    fun findSome(@Param(value = "lim") lim: Int): List<Idiom>
}
