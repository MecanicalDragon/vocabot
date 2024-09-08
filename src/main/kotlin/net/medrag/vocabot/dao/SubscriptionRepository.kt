package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface SubscriptionRepository : JpaRepository<Subscription, Long> {

    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE subscriptions SET last_word_id = last_word_id + :dif WHERE subscription_id = :subId"
    )
    @Transactional
    fun incrementWordIndexForSubscription(@Param(value = "subId") subId: Long, @Param(value = "dif") dif: Int): Int

    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE subscriptions SET last_word_id = :i WHERE subscription_id = :subId"
    )
    @Transactional
    fun setWordIndexForSubscription(@Param(value = "subId") subId: Long, @Param(value = "i") i: Int): Int

    @Modifying
    @Query(
        nativeQuery = true,
        value = "INSERT INTO learnings (sub_id, word_id) VALUES (:subId, :wordId)"
    )
    @Transactional
    fun addToLearn(@Param(value = "subId") subId: Long, @Param(value = "wordId") wordId: String): Int

    @Modifying
    @Query(
        nativeQuery = true,
        value = "DELETE FROM learnings WHERE sub_id = :subId AND word_id = :wordId"
    )
    @Transactional
    fun learned(@Param(value = "subId") subId: Long, @Param(value = "wordId") wordId: String): Int

    @Query(
        nativeQuery = true,
        value = "WITH word_ids as (SELECT word_id FROM learnings WHERE sub_id = :subId) SELECT * FROM word_ids ORDER BY RAND() LIMIT :lim"
    )
    fun getToLearn(@Param(value = "subId") subId: Long, @Param(value = "lim") lim: Int): List<String>
}
