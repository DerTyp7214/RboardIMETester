package de.dertyp7214.rboardimetester.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CheckCardData(
    @StringRes
    val text: Int,
    @DrawableRes
    val icon: Int,
    val isChecked: Boolean,
    val clickListener: (Boolean) -> Unit
)
