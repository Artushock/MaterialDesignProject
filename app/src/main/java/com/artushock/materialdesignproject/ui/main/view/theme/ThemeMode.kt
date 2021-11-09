package com.artushock.materialdesignproject.ui.main.view.theme

import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode(
   val mode: Int
) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM_MODE(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
}