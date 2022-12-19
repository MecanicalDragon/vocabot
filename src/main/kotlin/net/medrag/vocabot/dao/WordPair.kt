package net.medrag.vocabot.dao

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

/**
 * @author Stanislav Tretyakov
 * 18.04.2022
 */
@Entity
@Table(name = "vocabulary")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class WordPair(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val lang1: String,
    val lang2: String,
    @Type(type = "jsonb")
    var examples: List<String> = emptyList(),
)
