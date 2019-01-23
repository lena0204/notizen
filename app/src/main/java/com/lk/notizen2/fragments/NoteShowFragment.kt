package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.*
import com.lk.notizen2.utils.*
import kotlinx.android.synthetic.main.fragment_show.*
import kotlinx.android.synthetic.main.fragment_show.view.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteShowFragment: Fragment(), Observer<NoteEntity> {

    private val TAG = "NoteShowFragment"

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var actionViewModel: ActionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_show, container, false)
        root.tv_show_content.setOnClickListener { _ ->
            Log.d(TAG, "Clicked")
            actionViewModel.setAction(NotesAction.EDIT_NOTE)
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        actionViewModel = ViewModelFactory.getActionViewModel(requireActivity())
        notesViewModel.selectedNote.observe(this, this)
    }

    private fun printNote(note: NoteEntity){
        tv_show_id.text = note.id.toString()
        tv_show_title.text = note.title
        tv_show_content.text = note.content
        tb_show_protected.isChecked = note.getLockedAsEnum() == Lock.LOCKED
        tb_show_priority.isChecked = note.getPriorityAsEnum() == Priority.URGENT
        tv_show_category.text = note.getCategoryAsEnum().category
        setCategoryBar(note.getCategoryAsEnum())
    }

    private fun setCategoryBar(category: Category){
        iv_show_category.setImageResource(category.color)
    }

    override fun onChanged(selectedNote: NoteEntity?) {
        if(selectedNote != null){
            printNote(selectedNote)
        }
    }

    override fun onResume() {
        super.onResume()
        if(notesViewModel.selectedNote.value != null)
            printNote(notesViewModel.selectedNote.value!!)
    }
}