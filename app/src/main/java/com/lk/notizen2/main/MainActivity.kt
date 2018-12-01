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
import com.lk.notizen2.models.NotesViewModel

class MainActivity : FragmentActivity(), Observer<Any> {

    private val TAG = "MainActivity"
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_empty)
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        // TESTING_ notesViewModel.addTestNotes()
        supportFragmentManager.transaction { replace(R.id.fl_main_empty, NoteListFragment()) }
        notesViewModel.selectedNote.observe(this, this)
        notesViewModel.editNote.observe(this,this)
    }

    override fun onChanged(update: Any?) {
        if(update != null){
            // TODO onChanged: bessere Aufteilung der Fälle, BackStack korrekt konfigurieren
            when{
                update is NoteEntity && update.title != "" -> {
                    supportFragmentManager.transaction {
                        addToBackStack(null)
                        replace(R.id.fl_main_empty, NoteShowFragment())
                    }
                }
                update is NoteEntity && update.title == "" -> {
                    supportFragmentManager.transaction {
                        addToBackStack(null)
                        replace(R.id.fl_main_empty, NoteListFragment())
                    }
                }
                update == true -> {
                    supportFragmentManager.transaction {
                        addToBackStack(null)
                        replace(R.id.fl_main_empty, NoteEditFragment())
                    }
                }
            }
        }
    }
}
