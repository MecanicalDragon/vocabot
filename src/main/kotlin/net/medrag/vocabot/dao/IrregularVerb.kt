package net.medrag.vocabot.dao

import jakarta.persistence.*

/**
 * @author Stanislav Tretyakov
 * 18.05.2022
 */
@Entity
@Table(name = "irregular")
data class IrregularVerb(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val form1: String,
    val form2: String,
    val form3: String
)
