package com.lk.notizen2.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lk.notizen2.R
import com.lk.notizen2.main.MainActivity

/**
 * Erstellt von Lena am 23/01/2019.
 */
object Themer {

    const val TAG = "Themer"
    private lateinit var sharedprefs: SharedPreferences

    fun switchTheme(activity: MainActivity){
        sharedprefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val currentTheme = readThemeFromSharedPrefs()
        when(currentTheme){
            Design.THEME_LIGHT ->
                writeThemeToSharedPrefs(Design.THEME_DARK)
            Design.THEME_DARK ->
                writeThemeToSharedPrefs(Design.THEME_BLACK)
            Design.THEME_BLACK ->
                writeThemeToSharedPrefs(Design.THEME_LIGHT)
        }
        // IDEA_ ein ordentliches schwarzes Theme erstellen; Handling cardBackground?
        activity.recreate()
    }

    private fun readThemeFromSharedPrefs(): Design {
        val value = sharedprefs.getInt(Constants.PREF_DESIGN, 0)
        Log.d(TAG, "Theme: $value")
        return Design.values()[value]
    }

    private fun writeThemeToSharedPrefs(theme: Design){
        Log.d(TAG, "Theme: " + theme.ordinal)
        sharedprefs.edit {
            putInt(Constants.PREF_DESIGN, theme.ordinal)
        }
    }

    fun setThemeOnCreated(activity: MainActivity){
        sharedprefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val currentTheme = readThemeFromSharedPrefs()
        val themeID = when(currentTheme){
            Design.THEME_LIGHT -> R.style.AppThemeGreyBlack
            Design.THEME_DARK -> { /* nicht mehr notwendig, TODO Theme Konstanten aufräumen */
                R.style.AppThemeGreyBlack
            }
            Design.THEME_BLACK -> R.style.AppThemeGreyBlack
        }
        activity.setTheme(themeID)
    }

}