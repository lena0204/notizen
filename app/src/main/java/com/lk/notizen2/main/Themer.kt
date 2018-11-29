package com.lk.notizen2.main

import android.app.Activity
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.lk.notizen2.R
import com.lk.notizen2.utils.Constants
import com.lk.notizen2.utils.Priority
import java.util.*

/**
 * Erstellt von Lena am 09.10.18.
 */
object Themer {

    const val THEME_LIGHT = 0
    const val THEME_DARK = 1
    private val TAG = "Themer"
    private var iTheme: Int = THEME_LIGHT

    fun changeToTheme(activity: Activity, pTheme: Int) {
        iTheme = pTheme
        activity.recreate()
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        when (iTheme) {
            THEME_LIGHT -> {
                activity.setTheme(R.style.AppTheme)
                Log.d(TAG, "Changed to light theme")
            }
            THEME_DARK -> {
                activity.setTheme(R.style.AppThemeDark)
                Log.d(TAG, "Changed to dark theme")
            }
        }
    }

    fun isThemeApplied(cTheme: Int): Boolean = cTheme == iTheme

    fun applyThemeTimeDependant(act: Activity): Int {
        var theme = -1
        val sp = PreferenceManager.getDefaultSharedPreferences(act)
        val boPrefDynamic = sp.getBoolean(Constants.PREF_DYNAMIC, false)
        if (boPrefDynamic) {
            val current = Calendar.getInstance()
            val light = Calendar.getInstance()
            light.set(Calendar.HOUR_OF_DAY, 7)
            light.set(Calendar.MINUTE, 0)
            val dark = Calendar.getInstance()
            dark.set(Calendar.HOUR_OF_DAY, 20)
            dark.set(Calendar.MINUTE, 0)
            if (current.after(light) && current.before(dark)) {
                Log.d(TAG, "Helles Theme zeitabhängig anwenden.")
                sp.edit { putInt(Constants.PREF_DESIGN, THEME_LIGHT) }
            } else {
                Log.d(TAG, "Dunkles Theme zeitabhängig anwenden.")
                sp.edit { putInt(Constants.PREF_DESIGN, THEME_DARK) }
            }
        }
        val prefTheme = sp.getInt(Constants.PREF_DESIGN, THEME_LIGHT)
        if(!isThemeApplied(prefTheme))
            theme = prefTheme
        return theme
    }

    // passendes Prioritätsicon je nach Priorität und Theme zurückgeben
    fun getPriorityImage(priority: Priority, act: Activity): Drawable {
        var image = act.resources.getDrawable(R.mipmap.ic_priority)
        var color = act.resources.getColor(R.color.tint_reminder_light)
        if (isThemeApplied(THEME_LIGHT)) {
            // -1 setzt das Schloss Icon
            when(priority){
                Priority.URGENT -> color = act.resources.getColor(R.color.tint_urgent_light)
                Priority.URGENT_LOCKED, Priority.REMINDER_LOCKED -> {
                    image = act.resources.getDrawable(R.mipmap.ic_protected)
                    color = act.resources.getColor(R.color.tint_protected_light)
                }
                Priority.REMINDER, Priority.ALL ->  { /* ignored */ }
            }
        } else {
            when(priority){
                Priority.URGENT -> color = act.resources.getColor(R.color.tint_urgent_dark)
                Priority.URGENT_LOCKED, Priority.REMINDER_LOCKED -> {
                    image = act.resources.getDrawable(R.mipmap.ic_protected)
                    color = act.resources.getColor(R.color.tint_protected_dark)
                }
                Priority.REMINDER -> color = act.resources.getColor(R.color.tint_reminder_dark)
                Priority.ALL ->  { /* ignored */ }
            }
        }
        image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        return image
    }

}