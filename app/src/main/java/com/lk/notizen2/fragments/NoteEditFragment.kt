package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.Category
import com.lk.notizen2.models.NotesViewModel
import kotlinx.android.synthetic.main.activity_todo_edit.*
import kotlinx.android.synthetic.main.activity_todo_edit.view.*
import java.util.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteEditFragment: Fragment(), Observer<NoteEntity> {

    private val TAG = "NoteEditFragment"

    private lateinit var notesVielModel: NotesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_todo_edit, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesVielModel = ViewModelProviders.of(requireActivity()).get(NotesViewModel::class.java)
        notesVielModel.selectedNote.observe(this, this)
        bt_edit_save.setOnClickListener { _ ->
            val note = storeInputDataInNote()
            notesVielModel.updateNote(note)
        }
    }

    private fun printNote(note: NoteEntity){
        tv_edit_id.text = note.id.toString()
        et_edit_title.setText(note.title, TextView.BufferType.EDITABLE)
        et_edit_description.setText(note.content,TextView.BufferType.EDITABLE)
        layout_todo_edit.background = Category.createDrawableForColor(note.getCategoryAsEnum().color, resources)
        requireActivity().actionBar?.title = note.title
        // TODO Themeauswahl / mindestens Themetracking implementieren mit generellem Zugriff ???
        // TODO Bearbeitung: schöne Lösung für verschiedene Status-Icons
    }

    override fun onChanged(selectedNote: NoteEntity?) {
        if(selectedNote != null){
            printNote(selectedNote)
        }
    }

    private fun storeInputDataInNote(): NoteEntity{
        // TODO Speichern: nicht alle Daten werden mitgenommen
        val note = notesVielModel.selectedNote.value!!
        note.title = et_edit_title.text.toString()
        note.content = et_edit_description.text.toString()
        return note
    }

    override fun onResume() {
        super.onResume()
        if(notesVielModel.selectedNote.value != null)
            printNote(notesVielModel.selectedNote.value!!)
    }


}