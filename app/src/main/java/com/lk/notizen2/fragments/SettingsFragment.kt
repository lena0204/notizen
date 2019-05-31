package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.lk.notizen2.R
import com.lk.notizen2.dialogs.PasswordSetDialog
import com.lk.notizen2.main.PasswordChecker
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 02/03/2019.
 */
class SettingsFragment : PreferenceFragmentCompat(), PasswordSetDialog.DialogListener {

    private val TAG = "SettingsFragment"
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        addClickedListener()
        prepareFilterPreference()
        // setDynamicSummaries()
    }

    private fun addClickedListener(){
        /*findPreference(Constants.PREF_DESIGN).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    Themer.switchTheme(activity as MainActivity)
                    true
                }*/
        findPreference(Constants.PREF_CATEGORY_STANDARD).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    // TODO Dialog mit Titel und Zeilenanzahl anzeigen
                    true
                }
        findPreference(Constants.PREF_PASSWORD_SET).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    callSetPasswordDialog()
                    true
                }
    }

    private fun prepareFilterPreference() {
        val sorting = findPreference(Constants.PREF_FILTER) as ListPreference
        sorting.entries = Categories.getCategoryArray()
        sorting.entryValues = arrayOf("0","1","2","3","4","5","6","7")
        Log.d(TAG, "prepared Filter preference")
        sorting.setOnPreferenceChangeListener { _, newValue ->
            val filterCategoryNumber: Int = newValue.toString().toInt()
            notesViewModel.filterCategory.value = Categories.getCategory(filterCategoryNumber)
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

    private fun callSetPasswordDialog() {
        val dialog: Fragment = PasswordSetDialog()
        requireActivity().supportFragmentManager
            .beginTransaction().add(dialog, "Dialog_set_password").commit()

    }

    override fun dialogResult(password1: String, password2: String) {
        if(PasswordChecker.checkNewPasswords(password1, password2) ) {
            val sp = PreferenceManager.getDefaultSharedPreferences(activity)
            sp.edit {
                putString(Constants.PREF_PASSWORD, password1)
            }
        }
    }

}