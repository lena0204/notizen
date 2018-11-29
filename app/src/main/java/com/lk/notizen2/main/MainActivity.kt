package com.lk.notizen2.main

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.R
import com.lk.notizen2.fragments.NoteListFragment
import com.lk.notizen2.models.NotesViewModel

class MainActivity : FragmentActivity(), Observer<Any> {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_empty)
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        notesViewModel.addTestNotes()
        supportFragmentManager.transaction { replace(R.id.fl_main_empty, NoteListFragment()) }
    }

    override fun onChanged(update: Any?) {

    }
}
