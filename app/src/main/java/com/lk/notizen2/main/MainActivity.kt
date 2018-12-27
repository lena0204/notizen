package com.lk.notizen2.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.fragments.*
import com.lk.notizen2.models.ActionViewModel
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.NotesAction
import com.lk.notizen2.utils.ViewModelFactory

class MainActivity : FragmentActivity(), Observer<NotesAction> {

    private val TAG = "MainActivity"
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var actionViewModel: ActionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_empty)

        notesViewModel = ViewModelFactory.getNotesViewModel(this)
        actionViewModel = ViewModelFactory.getActionViewModel(this)
        actionViewModel.setObserver(this,this)

        supportFragmentManager.transaction { replace(R.id.fl_main_empty, NoteListFragment()) }
    }

    override fun onChanged(update: NotesAction) {
        // TODO Backstack konfigurieren
        when (update){
             NotesAction.SHOW_NOTE -> {
                supportFragmentManager.transaction {
                    addToBackStack(null)
                    replace(R.id.fl_main_empty, NoteShowFragment())
                }
            }
            NotesAction.SHOW_LIST -> {
                supportFragmentManager.transaction {
                    addToBackStack(null)
                    replace(R.id.fl_main_empty, NoteListFragment())
                }
            }
            NotesAction.EDIT_NOTE -> {
                supportFragmentManager.transaction {
                    addToBackStack(null)
                    replace(R.id.fl_main_empty, NoteEditFragment())
                }
            }
            NotesAction.NEW_NOTE -> supportFragmentManager.transaction {
                addToBackStack(null)
                replace(R.id.fl_main_empty, NoteEditFragment())
            }
            NotesAction.SHOW_PREFERENCES -> TODO()
            NotesAction.NONE -> { }
        }

    }
}
