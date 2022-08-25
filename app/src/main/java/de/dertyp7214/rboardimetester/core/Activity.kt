package de.dertyp7214.rboardimetester.core

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import de.dertyp7214.rboardcomponents.core.getAttr

fun Activity.openUrl(url: String) {
    val color = getAttr(com.google.android.material.R.attr.colorSurface)
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams
                .Builder()
                .setToolbarColor(color)
                .setNavigationBarColor(color)
                .setSecondaryToolbarColor(color)
                .setNavigationBarDividerColor(color)
                .build()
        )
        .build()
        .launchUrl(this, Uri.parse(url))
}