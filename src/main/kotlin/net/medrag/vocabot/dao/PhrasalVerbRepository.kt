package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
interface PhrasalVerbRepository : JpaRepository<PhrasalVerb, Int> {
    @Query(nativeQuery = true, value = "SELECT *  FROM phrasal ORDER BY RAND() LIMIT :lim")
    fun findSome(@Param(value = "lim") lim: Int): List<PhrasalVerb>
}
