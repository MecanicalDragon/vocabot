package net.medrag.vocabot.dao

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.Type

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Entity
@Table(name = "vocabulary")
data class WordPair(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val lang1: String,
    val lang2: String,
    @Type(JsonBinaryType::class)
    var examples: List<String> = emptyList()
)
