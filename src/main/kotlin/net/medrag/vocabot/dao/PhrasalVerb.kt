package net.medrag.vocabot.dao

import jakarta.persistence.*

/**
 * @author Stanislav Tretiakov
 * 17.06.2023
 */
@Entity
@Table(name = "phrasal")
data class PhrasalVerb(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val verb: String,
    val meaning: String,
    val examples: String
)
