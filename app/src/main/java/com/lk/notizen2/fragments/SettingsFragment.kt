package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.lk.notizen2.R
import com.lk.notizen2.dialogs.PasswordSetDialog
import com.lk.notizen2.dialogs.PersonalizationDialog
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 02/03/2019.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val TAG = "SettingsFragment"
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var spw: SharedPrefWrapper

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        spw = SharedPrefWrapper(requireContext())
        addClickedListener()
        prepareFilterPreference()
        preparePasswordPreferences()
    }

    private fun addClickedListener(){
        findPreference(Constants.PREF_CAT_PERSONALIZATION).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    // TODO Dialog mit Titel und Zeilenanzahl anzeigen
                    callPersonalizationDialog()
                    true
                }
        findPreference(Constants.PREF_PASSWORD_SET).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    callSetPasswordDialog()
                    true
                }
        findPreference(Constants.PREF_DESIGN).setOnPreferenceChangeListener { preference, newValue ->
            requireActivity().recreate()
            true
        }
    }

    private fun prepareFilterPreference() {
        val sorting = findPreference(Constants.PREF_FILTER_CATEGORIES) as MultiSelectListPreference
        sorting.entries = Categories.getCategoryNameArray()
        sorting.entryValues = arrayOf("0","1","2","3","4","5","6","7")

        sorting.setOnPreferenceChangeListener { _, newValue ->
            Log.d(TAG, "Neuer Wert der FilterPreference: $newValue")
            val valueSet = newValue as Set<String>
            val valueList = Categories.transformToCategoryList(valueSet)
            notesViewModel.setFilteredCategories(valueList)
            true
        }
    }

    private fun preparePasswordPreferences() {
        val password = spw.readString(Constants.SPREF_PASSWORD)
        if(password != "") {
            findPreference(Constants.PREF_PASSWORD_SET).isEnabled = false
        }
    }

    private fun callSetPasswordDialog() {
        val dialog: Fragment = PasswordSetDialog()
        requireActivity().supportFragmentManager
            .beginTransaction().add(dialog, "Dialog_set_password").commit()
    }

    private fun callPersonalizationDialog() {
        val dialog: Fragment = PersonalizationDialog()
        requireActivity().supportFragmentManager
            .beginTransaction().add(dialog, "Dialog_personalization").commit()
    }

}