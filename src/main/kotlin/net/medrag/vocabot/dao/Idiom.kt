package net.medrag.vocabot.dao

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Entity
@Table(name = "idioms")
data class Idiom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val idiom: String,
    val meaning: String,
    @Type(JsonBinaryType::class)
    var examples: List<String> = emptyList()
)
