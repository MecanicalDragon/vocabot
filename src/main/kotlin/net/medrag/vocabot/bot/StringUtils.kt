package net.medrag.vocabot.bot

/**
 * @author Stanislav Tretiakov
 * 29.07.2023
 */
fun bold(string: String) = TEXT_BOLD_START + string + TEXT_BOLD_END
fun italic(string: String) = TEXT_ITALIC_START + string + TEXT_ITALIC_END
fun underlined(string: String) = TEXT_UNDER_START + string + TEXT_UNDER_END
fun crossed(string: String) = TEXT_CROSSED_START + string + TEXT_CROSSED_END
fun spoiler(string: String) = TEXT_SPOILER_START + string + TEXT_SPOILER_END
