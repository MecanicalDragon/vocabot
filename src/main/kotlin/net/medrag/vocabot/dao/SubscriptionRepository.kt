package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface SubscriptionRepository : JpaRepository<Subscription, Long> {

    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE subscriptions SET last_word_id = last_word_id + :l WHERE subscription_id = :n"
    )
    fun incrementWordIndex(@Param(value = "n") n: Long, @Param(value = "l") l: Int): Int

    @Modifying
    @Query(
        nativeQuery = true,
        value = "UPDATE subscriptions SET last_word_id = :l WHERE subscription_id = :n"
    )
    fun setWordIndex(@Param(value = "n") n: Long, @Param(value = "l") l: Int): Int
}
