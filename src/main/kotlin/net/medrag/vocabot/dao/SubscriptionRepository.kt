package net.medrag.vocabot.dao

import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
interface SubscriptionRepository : JpaRepository<Subscription, Long>
