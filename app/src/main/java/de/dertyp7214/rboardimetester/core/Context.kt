package de.dertyp7214.rboardimetester.core

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("DiscouragedApi")
fun Context.getStringIdByName(name: String) =
    resources.getIdentifier(name, "string", packageName)

@SuppressLint("DiscouragedApi")
fun Context.getDrawableIdByName(name: String) =
    resources.getIdentifier(name, "drawable", packageName)