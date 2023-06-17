package net.medrag.vocabot.model

/**
 * @author Stanislav Tretyakov
 * 18.05.2022
 */
data class IrregularVerbDto(
    val form1: String,
    val form2: String,
    val form3: String
) {
    override fun toString(): String {
        return "Irregular verb:\n$form1 - $form2 - $form3"
    }
}
