package com.lk.notizen2.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    override fun onChanged(update: NotesAction) {
        // TESTING_ Backstack konfigurieren
        when (update){
             NotesAction.SHOW_NOTE -> {
                supportFragmentManager.transaction {
                    addToBackStack(null)
                    replace(R.id.fl_main_empty, NoteShowFragment())
                }
            }
            NotesAction.SHOW_LIST -> {
                supportFragmentManager.transaction {
                    replace(R.id.fl_main_empty, NoteListFragment())
                }
            }
            NotesAction.EDIT_NOTE, NotesAction.NEW_NOTE -> {
                supportFragmentManager.transaction {
                    addToBackStack(null)
                    replace(R.id.fl_main_empty, NoteEditFragment())
                }
            }
            NotesAction.SHOW_PREFERENCES -> TODO()
            NotesAction.NONE -> {  }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val value =  super.onCreateOptionsMenu(menu)
        this.menuInflater.inflate(R.menu.menu_main, menu)
        return value
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val value = super.onOptionsItemSelected(item)
        if(item?.itemId == R.id.menu_themeswitch){
            Themer.switchTheme(this)
        }
        return value
    }
}
