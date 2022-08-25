package de.dertyp7214.rboardimetester.core

import java.util.*

fun String.capitalize(locale: Locale = Locale.getDefault()) =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }