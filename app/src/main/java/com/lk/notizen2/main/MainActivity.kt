package com.lk.notizen2.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import com.lk.notizen2.R
import com.lk.notizen2.fragments.*
import com.lk.notizen2.models.ActionViewModel
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.*

class MainActivity : FragmentActivity(), Observer<NotesAction> {

    private val TAG = "MainActivity"
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var actionViewModel: ActionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.setThemeOnCreated(this)
        setContentView(R.layout.activity_main_empty)

        notesViewModel = ViewModelFactory.getNotesViewModel(this)
        actionViewModel = ViewModelFactory.getActionViewModel(this)
        actionViewModel.setObserver(this,this)

        supportFragmentManager.transaction { replace(R.id.fl_main_empty, NoteListFragment()) }
    }

    // TODO Permission Request fur Write / Read external storage, manuell nötig
    // TODO Asynchron umsetzen

    override fun onChanged(update: NotesAction) {
        when (update){
            NotesAction.SHOW_NOTE -> showNoteTransaction()
            NotesAction.SHOW_LIST -> showListTransaction()
            NotesAction.EDIT_NOTE, NotesAction.NEW_NOTE -> editNoteTransaction()
            NotesAction.SHOW_PREFERENCES -> TODO()
            NotesAction.NONE -> {  }
        }
    }

    private fun showNoteTransaction(){
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val value =  super.onCreateOptionsMenu(menu)
        this.menuInflater.inflate(R.menu.menu_main, menu)
        return value
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val value = super.onOptionsItemSelected(item)
        when(item?.itemId){
            R.id.menu_themeswitch -> Themer.switchTheme(this)
            R.id.menu_backup -> tryNotesBackup()
            R.id.menu_restore -> tryNotesRestore()
        }
        return value
    }

    private fun tryNotesBackup() {
        val result = BackupRestore.backupNotes(notesViewModel.getNotes().value!!)
        if(result) {
            createToast(R.string.toast_backup)
        } else {
            createToast(R.string.toast_b_failed)
        }
    }

    private fun tryNotesRestore() {
        val result = BackupRestore.restoreNotes(this)
        if(result) {
            createToast(R.string.toast_restored)
        } else {
            createToast(R.string.toast_r_failed)
        }
    }

    private fun createToast(text: Int){
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}
