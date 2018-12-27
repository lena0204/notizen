package com.lk.notizen2.fragments

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.dialogs.CategoryDialog
import com.lk.notizen2.models.*
import com.lk.notizen2.utils.*
import kotlinx.android.synthetic.main.activity_todo_edit.*
import java.util.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteEditFragment: Fragment(), Observer<Any> {

    private val TAG = "NoteEditFragment"

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var actionViewModel: ActionViewModel
    private var priority: Priority = Priority.ALL
    private var category = Categories.WHITE
    private var isNewNote = false
    private var wasSaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        val root = inflater.inflate(R.layout.activity_todo_edit, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        actionViewModel = ViewModelFactory.getActionViewModel(requireActivity())
        notesViewModel.selectedNote.observe(this, this)
        notesViewModel.selectedCategory.observe(this,this)
        bt_edit_save.setOnClickListener { _ ->
            saveNote(saveButton = true)
            wasSaved = true
            actionViewModel.setAction(NotesAction.SHOW_LIST)
        }
        bt_edit_category.setOnClickListener { _ ->
            requireActivity().supportFragmentManager.transaction {
                add(CategoryDialog(), "CAT_DIALOG")
            }
        }
    }

    private fun printNote(note: NoteEntity){
        // wasSaved = false
        priority = note.getPriorityAsEnum()
        val locked = note.getLockedAsEnum()
        tv_edit_id.text = note.id.toString()
        et_edit_title.setText(note.title, TextView.BufferType.EDITABLE)
        et_edit_description.setText(note.content,TextView.BufferType.EDITABLE)
        tb_edit_priority.isChecked = (priority == Priority.URGENT)
        tb_edit_protected.isChecked = (locked == Lock.LOCKED)
        notesViewModel.selectedCategory.value = note.getCategoryAsEnum()
        requireActivity().actionBar?.title = note.title
        // TODO Themeauswahl / mindestens Themetracking implementieren mit generellem Zugriff ???
    }

// TODO nach dem Speichern taucht kurz die default version auf -> verhindern
    override fun onChanged(update: Any?) {
        if(update is NoteEntity?) {
            isNewNote = !isValidNote(update)
            if (isValidNote(update)) {
                printNote(update!!)
            } else {
                printDefault()
            }
        } else if(update is Category?){
            if(update != null) {
                onCategoryChosen(update)
            }
        }
    }

    private fun printDefault(){
        tb_edit_priority.isChecked = false
        tb_edit_protected.isChecked = false
        requireActivity().actionBar?.title = resources.getString(R.string.new_note)
    }

    private fun isValidNote(note: NoteEntity?) = (note != null && note.id != 0)

    private fun storeInputDataInNote(): NoteEntity {
        val note = if(isNewNote) {
            NoteEntity()
        } else {
            notesViewModel.selectedNote.value!!
        }
        note.title = et_edit_title.text.toString()
        note.content = et_edit_description.text.toString()
        note.date = DateFormat.format("yyyy/MM/dd HH:mm:ss", Date().time).toString()
        if(tb_edit_priority.isChecked) {
            note.setPriorityAsEnum(Priority.URGENT)
        } else {
            note.setPriorityAsEnum(Priority.REMINDER)
        }

        if(tb_edit_protected.isChecked) {
            note.setLockedAsEnum(Lock.LOCKED)
        } else {

            note.setLockedAsEnum(Lock.UNLOCKED)
        }
        note.setCategoryAsEnum(category)
        return note
    }

    override fun onPause(){
        super.onPause()
        Log.v(TAG,"onpause")
        saveNote()
    }

    private fun onCategoryChosen(newCategory: Category) {
        category = newCategory
        iv_edit_category.setImageResource(newCategory.color)
        bt_edit_category.setBackgroundColor(requireActivity().resources.getColor(newCategory.color))
    }

    private fun saveNote(saveButton: Boolean = false){
        if (!wasSaved) {
            Log.d(TAG, "saving with button: $saveButton")
            val note = storeInputDataInNote()
            if (isNewNote)
                notesViewModel.insertNote(note, saveButton)
            else
                notesViewModel.updateNote(note, saveButton)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onresume")
        val note = notesViewModel.selectedNote.value
        isNewNote = !isValidNote(note)
        wasSaved = false
        if(isValidNote(note))
            printNote(notesViewModel.selectedNote.value!!)
        else
            printDefault()
    }

}