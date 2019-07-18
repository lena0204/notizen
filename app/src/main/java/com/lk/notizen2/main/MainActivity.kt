package com.lk.notizen2.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
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
    private lateinit var permRequester: PermissionRequester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spw = SharedPrefWrapper(this)
        val designIsDark = spw.readBoolean(Constants.PREF_DESIGN)
        if (designIsDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_main_empty)

        permRequester = PermissionRequester(this)
        viewModel = ViewModelFactory.getNotesViewModel(this)
        viewModel.observeListAndActions(this, this)
        val filterSet = spw.readSet(Constants.PREF_FILTER_CATEGORIES, setOf(Categories.ALL.id.toString()))
        val filterList = Categories.transformToCategoryList(filterSet)
        viewModel.setFilteredCategories(filterList)

        changeToFragment(NoteListFragment(), false)
    }

    // TODO Permission Request fur Write / Read external storage, manuell nötig

    override fun onChanged(update: Any?) {
        Log.v(TAG, "Main: $update")
        when (update) {
            NavigationActions.SHOW_NOTE -> checkNoteProtection()
            NavigationActions.SHOW_LIST -> changeToFragment(NoteListFragment(), false)
            NavigationActions.EDIT_NOTE,
                NavigationActions.NEW_NOTE -> changeToFragment(NoteEditFragment())
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
                // PROBLEM_ es wird immer die Notiz angezeigt, nicht das Ziel bei geschützem löschen
                changeToFragment(NoteShowFragment())
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
            changeToFragment(NoteShowFragment())
            Log.d(TAG, "Notiz direkt anzeigen, weil offen")
        }
    }

    private fun changeToFragment(fragment: Fragment, addToStack: Boolean = true) {
        supportFragmentManager.transaction {
            if(addToStack) {
                addToBackStack(null)
            }
            replace(R.id.fl_main_empty, fragment)
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
            R.id.menu_backup -> tryNotesBackup()
            R.id.menu_restore -> tryNotesRestore()
            R.id.menu_settings -> changeToFragment(SettingsFragment())
        }
        return value
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PermissionRequester.PERMISSION_REQUEST_READ -> {
                if(isPermissionGranted(grantResults)) {
                    tryNotesRestore()
                } else {
                    createToast("READ Berechtigung verweigert")
                }
            }
            PermissionRequester.PERMISSION_REQUEST_WRITE -> {
                if(isPermissionGranted(grantResults)) {
                    tryNotesBackup()
                } else {
                    createToast("WRITE Berechtigung verweigert")
                }
            }
        }
    }
    private fun isPermissionGranted(grantResults: IntArray): Boolean
            = (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)

    private fun tryNotesBackup() {
        if(permRequester.checkWritePermission()) {
            BackupRestoreWrapper(viewModel).backupNotes(this)
        }
    }

    override fun onBackupFinished(result: Result) {
        when(result) {
            Result.RESULT_SUCCESS -> createToast(R.string.toast_backup)
            Result.RESULT_FAILED -> createToast(R.string.toast_b_failed)
        }
    }

    private fun tryNotesRestore() {
        if(permRequester.checkReadPermission()) {
            BackupRestoreWrapper(viewModel).restoreNotes()
        }
        // TODO Nutzerrückmeldung notwendig??
    }

    private fun createToast(text: Int) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    private fun createToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}
