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
@Table(name = "idioms")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class Idiom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val idiom: String,
    val meaning: String,
    @Type(type = "jsonb")
    var examples: List<String> = emptyList(),
)
