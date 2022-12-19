package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * @author Stanislav Tretyakov
 * 19.12.2022
 */
interface IdiomRepository : JpaRepository<Idiom, Int> {

    @Query(nativeQuery = true, value = "SELECT * FROM idioms ORDER BY random() LIMIT 1")
    fun findRandom(): Idiom
}
