package com.artushock.materialdesignproject.ui.main.view.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

private const val NO_PREFERENCES_RESPONSE: Int = -1
private const val THEME_PREFERENCES: String = "THEME_PREFERENCES"
private const val APP_THEME: String = "APP_THEME"
private const val APP_THEME_MODE: String = "APP_THEME_MODE"


class ThemePreferencesUtil(private val context: Context) {

    fun setAppTheme() {
        val sharedPreferences = getSharedPreferences()

        var theme = sharedPreferences.getInt(APP_THEME, NO_PREFERENCES_RESPONSE)
        if (theme == NO_PREFERENCES_RESPONSE) {
            theme = AppTheme.DEFAULT.theme
            sharedPreferences.edit().putInt(APP_THEME, theme).apply()
        }
        context.setTheme(theme)

        var themeMode = sharedPreferences.getInt(APP_THEME_MODE, NO_PREFERENCES_RESPONSE)
        if (themeMode == NO_PREFERENCES_RESPONSE) {
            themeMode = ThemeMode.SYSTEM_MODE.mode
            sharedPreferences.edit().putInt(APP_THEME_MODE, themeMode).apply()
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun getThemeFromPreferences(): Int {
        val sharedPreferences = getSharedPreferences()
        return sharedPreferences.getInt(APP_THEME, AppTheme.DEFAULT.theme)
    }

    fun getThemeModeFromPreferences(): Int {
        val sharedPreferences = getSharedPreferences()
        return sharedPreferences.getInt(APP_THEME_MODE, ThemeMode.SYSTEM_MODE.mode)
    }

    fun changeTheme(theme: AppTheme) {
        val sharedPreferences = getSharedPreferences()
        sharedPreferences.edit().putInt(APP_THEME, theme.theme).apply()
    }

    fun changeThemeMode(themeMode: ThemeMode) {
        val sharedPreferences = getSharedPreferences()
        sharedPreferences.edit().putInt(APP_THEME_MODE, themeMode.mode).apply()
    }

    private fun getSharedPreferences(): SharedPreferences = context.getSharedPreferences(
        THEME_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    )
}