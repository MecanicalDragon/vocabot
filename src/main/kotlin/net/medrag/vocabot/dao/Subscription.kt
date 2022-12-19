package net.medrag.vocabot.dao

import javax.persistence.*

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Entity
@Table(name = "subscriptions")
data class Subscription(
    @Id
    @Column(name = "subscription_id")
    val subId: Long,
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "chat_type")
    val chatType: ChatType,
    @Column(name = "name")
    val name: String
)

enum class ChatType {
    USER,
    CHAT
}
