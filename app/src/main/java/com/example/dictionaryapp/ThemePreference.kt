package com.example.dictionaryapp

import android.content.Context
import android.content.SharedPreferences


// To maintain the night theme
object ThemePreference {
    private const val PREFS_NAME = "theme_pref"
    private const val KEY_THEME = "key_theme"
    private const val THEME_LIGHT = "light"
    private const val THEME_DARK = "dark"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setTheme(context: Context, isNightMode: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_THEME, if (isNightMode) THEME_DARK else THEME_LIGHT)
        editor.apply()
    }

    fun isNightMode(context: Context): Boolean {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_THEME, THEME_LIGHT) == THEME_DARK
    }
}
