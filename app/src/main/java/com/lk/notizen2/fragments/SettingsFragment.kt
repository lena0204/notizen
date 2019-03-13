package com.lk.notizen2.fragments

import android.os.Bundle
import android.provider.SyncStateContract
import androidx.preference.*
import com.lk.notizen2.R
import com.lk.notizen2.main.MainActivity
import com.lk.notizen2.utils.Constants
import com.lk.notizen2.utils.Themer

/**
 * Erstellt von Lena am 02/03/2019.
 */
class SettingsFragment : PreferenceFragment() {

    private val TAG = "SettingsFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.fragment_settings)
        addClickedListener()
        setDynamicSummaries()
    }

    private fun addClickedListener(){
        findPreference(Constants.PREF_DESIGN).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    Themer.switchTheme(activity as MainActivity)
                    true
                }
        findPreference(Constants.PREF_CATEGORY_STANDARD).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    // TODO Dialog mit Titel und Zeilenanzahl anzeigen
                    true
                }
    }

    private fun setDynamicSummaries(){
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        val appTheme = sp.getInt(Constants.PREF_DESIGN, 0)
        val designPreference = findPreference(Constants.PREF_DESIGN)
        when(appTheme){
            0 -> designPreference.setSummary(R.string.pref_theme_summary_light)
            1 -> designPreference.setSummary(R.string.pref_theme_summary_dark)
            2 -> designPreference.setSummary(R.string.pref_theme_summary_black)
        }
    }

}