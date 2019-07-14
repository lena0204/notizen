package com.lk.notizen2.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import com.lk.backuprestore.Result
import com.lk.backuprestore.listener.OnBackupFinished
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.dialogs.PasswordSetDialog
import com.lk.notizen2.dialogs.ProtectionDialog
import com.lk.notizen2.fragments.*
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.*

class MainActivity : AppCompatActivity(),
    Observer<Any>,
    ProtectionDialog.DialogListener,
    PasswordSetDialog.DialogListenerPasswordSet,
    OnBackupFinished {

    private val TAG = "MainActivity"
    private lateinit var viewModel: NotesViewModel
    private lateinit var spw: SharedPrefWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // IDEA_ Themer.setThemeOnCreated(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setContentView(R.layout.activity_main_empty)

        viewModel = ViewModelFactory.getNotesViewModel(this)
        viewModel.observeListAndActions(this, this)
        spw = SharedPrefWrapper(this)
        val filterSet = spw.readSet(Constants.PREF_FILTER_CAT_MULTI, setOf(Categories.ALL.id.toString()))
        val filterList = Categories.transformToCategoryList(filterSet)
        viewModel.setFilteredCategories(filterList)

        supportFragmentManager.transaction { replace(R.id.fl_main_empty, NoteListFragment()) }
    }

    // TODO Permission Request fur Write / Read external storage, manuell nötig

    override fun onChanged(update: Any?) {
        Log.v(TAG, "Main: $update")
        when (update) {
            NavigationActions.SHOW_NOTE -> checkNoteProtection()
            NavigationActions.SHOW_LIST -> showListTransaction()
            NavigationActions.EDIT_NOTE,
                NavigationActions.NEW_NOTE -> editNoteTransaction()
            NavigationActions.SHOW_PREFERENCES -> { } // NOT needed in current implementation
            NavigationActions.DELETE_NOTE -> {}
            NavigationActions.SAVE_NOTE -> {}
        }
    }

    override fun dialogResultSetter(password1: String, password2: String) {
        if(PasswordChecker.checkNewPasswords(password1, password2) ) {
            spw.writeString(Constants.SPREF_PASSWORD, password1)
            createToast(R.string.toast_new_password)
        }
    }

    override fun dialogResultProtection(value: String) {
        if (value == ProtectionDialog.LOCK_DIALOG_CANCELLED) {
            Log.d(TAG, "Passwortabfrage wurde gecancelt.")
        } else {
            val isPasswordCorrect = checkPassword(value)
            if (isPasswordCorrect) {
                showNoteTransaction()   // PROBLEM_ es wird immer die Notiz angezeigt, nicht das Ziel bei geschützem löschen
                Log.d(TAG, "Notiz anzeigen (Passwort)")
            } else {
                createToast("Das Passwort ist falsch.")
            }
        }
    }

    private fun checkPassword(value: String): Boolean {
        val truePassword = spw.readString(Constants.SPREF_PASSWORD)
        Log.d(TAG, "Passwort prüfen: (Eingabe) $value == $truePassword (korrekt)")
        return PasswordChecker.isInputPasswordCorrect(value, truePassword)
    }

    private fun checkNoteProtection() {
        val note: NoteEntity = viewModel.getSelectedNote()
        if (note.isProtected()) {
            supportFragmentManager.transaction {
                add(ProtectionDialog(), "Release lock dialog")
            }
        } else {
            showNoteTransaction()
            Log.d(TAG, "Notiz direkt anzeigen, weil offen")
        }
    }

    private fun showNoteTransaction() {
        supportFragmentManager.transaction {
            addToBackStack(null)
            replace(R.id.fl_main_empty, NoteShowFragment())
        }
    }

    private fun showListTransaction() {
        supportFragmentManager.transaction {
            replace(R.id.fl_main_empty, NoteListFragment())
        }
    }

    private fun editNoteTransaction() {
        supportFragmentManager.transaction {
            addToBackStack(null)
            replace(R.id.fl_main_empty, NoteEditFragment())
        }
    }

    private fun showPreferences() {
        supportFragmentManager.transaction {
            addToBackStack(null)
            replace(R.id.fl_main_empty, SettingsFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val value = super.onCreateOptionsMenu(menu)
        this.menuInflater.inflate(R.menu.menu_main, menu)
        return value
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val value = super.onOptionsItemSelected(item)
        when (item?.itemId) {
            // R.id.menu_themeswitch -> { /* TODO test ohne Themer.switchTheme(this)*/ }
            R.id.menu_backup -> tryNotesBackup()
            R.id.menu_restore -> tryNotesRestore()
            R.id.menu_settings -> showPreferences()
        }
        return value
    }

    private fun tryNotesBackup() {
        BackupRestoreWrapper(viewModel).backupNotes(this)
    }

    override fun onBackupFinished(result: Result) {
        when(result) {
            Result.RESULT_SUCCESS -> createToast(R.string.toast_backup)
            Result.RESULT_FAILED -> createToast(R.string.toast_b_failed)
        }
    }

    private fun tryNotesRestore() {
        BackupRestoreWrapper(viewModel).restoreNotes()
        // TODO Nutzerrückmeldung notwendig??
    }

    private fun createToast(text: Int) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    private fun createToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}
