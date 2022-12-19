package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * @author Stanislav Tretyakov
 * 18.05.2022
 */
interface IrregularVerbRepository : JpaRepository<IrregularVerb, Int> {

    @Query(nativeQuery = true, value = "SELECT *  FROM irregular ORDER BY random() LIMIT 1")
    fun findRandom(): IrregularVerb
}
