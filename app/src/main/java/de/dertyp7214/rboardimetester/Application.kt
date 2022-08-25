package de.dertyp7214.rboardimetester

import de.dertyp7214.rboardcomponents.utils.ThemeUtils

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        ThemeUtils.applyTheme(this)
        ThemeUtils.registerActivityLifecycleCallbacks(this)
    }
}