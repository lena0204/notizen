package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.Category
import com.lk.notizen2.models.NotesViewModel
import kotlinx.android.synthetic.main.activity_todo_show.*
import kotlinx.android.synthetic.main.activity_todo_show.view.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteShowFragment: Fragment(), Observer<NoteEntity> {

    private val TAG = "NoteShowFragment"

    private lateinit var notesVielModel: NotesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_todo_show, container, false)
        root.tv_show_content.setOnClickListener { _ ->
            Log.d(TAG, "Clicked")
            notesVielModel.editCurrentSelectedNote()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesVielModel = ViewModelProviders.of(requireActivity()).get(NotesViewModel::class.java)
        notesVielModel.selectedNote.observe(this, this)
    }

    private fun printNote(note: NoteEntity){
        tv_show_id.text = note.id.toString()
        tv_show_title.text = note.title
        tv_show_content.text = note.content
        fl_show_wallpaper.background = Category.createDrawableForColor(note.getCategoryAsEnum().color, resources)
        requireActivity().actionBar?.title = note.title
        // TODO mindestens Themetracking implementieren mit generellem Zugriff ??
        // TODO Anzeige: schöne Lösung für verschiedene Status-Icons
    }

    override fun onChanged(selectedNote: NoteEntity?) {
        if(selectedNote != null){
            printNote(selectedNote)
        }
    }

    override fun onResume() {
        super.onResume()
        if(notesVielModel.selectedNote.value != null)
            printNote(notesVielModel.selectedNote.value!!)
    }
}