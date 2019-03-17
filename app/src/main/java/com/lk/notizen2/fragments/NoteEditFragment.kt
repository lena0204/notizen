package com.lk.notizen2.fragments

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import com.lk.notizen2.R
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.dialogs.CategoryDialog
import com.lk.notizen2.models.*
import com.lk.notizen2.utils.*
import kotlinx.android.synthetic.main.fragment_edit.*
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
    private var currentNote = NoteEntity()
    private var isNewNote = false
    private var wasSaved = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saved: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseViewModels()
        setClickListeners()
    }

    private fun initialiseViewModels(){
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        actionViewModel = ViewModelFactory.getActionViewModel(requireActivity())
        notesViewModel.selectedNote.observe(this, this)
        notesViewModel.selectedCategory.observe(this,this)
    }

    private fun setClickListeners(){
        bt_edit_save.setOnClickListener { saveNoteIfNotEmpty() }
        bt_edit_category.setOnClickListener { showCategoryDialogue() }
    }

    private fun showCategoryDialogue(){
        requireActivity().supportFragmentManager.transaction {
            add(CategoryDialog(), "CAT_DIALOG")
        }
    }

    private fun onCategoryChosen(newCategory: Category) {
        category = newCategory
        iv_edit_category.setImageResource(newCategory.color)
        bt_edit_category.imageTintList =
            ColorStateList.valueOf(requireActivity().resources.getColor(newCategory.color))
        bt_edit_category.imageTintMode = PorterDuff.Mode.SRC_ATOP
    }

    private fun printNote(note: NoteEntity){
        priority = note.getPriorityAsEnum()
        val locked = note.getLockedAsEnum()
        tv_edit_id.text = note.id.toString()
        et_edit_title.setText(note.title, TextView.BufferType.EDITABLE)
        et_edit_description.setText(note.content,TextView.BufferType.EDITABLE)
        tb_edit_priority.isChecked = (priority == Priority.URGENT)
        tb_edit_protected.isChecked = (locked == Lock.LOCKED)
        notesViewModel.selectedCategory.value = note.getCategoryAsEnum()
    }

    private fun printDefaultNote(){
        tb_edit_priority.isChecked = false
        tb_edit_protected.isChecked = false
        onCategoryChosen(Categories.WHITE)
    }

    override fun onPause(){
        super.onPause()
        Log.v(TAG,"onPause")
        saveNote()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onResume")
        val note = notesViewModel.selectedNote.value
        wasSaved = false
        updateNote(note)
    }

    // TODO nach dem Speichern taucht kurz die default version (Farbe und Buttons) auf -> verhindern
    override fun onChanged(update: Any?) {
        when {
            update is NoteEntity? && update != null -> {
                updateNote(update)
            }
            update is Category? && update != null -> {
                onCategoryChosen(update)
            }
        }
    }

    private fun isValidNote(note: NoteEntity?) = (note != null && note.id != 0)

    private fun updateNote(updatedNote: NoteEntity?){
        if (isValidNote(updatedNote)) {
            printNote(updatedNote!!)
        } else {
            isNewNote = true
            printDefaultNote()
        }
    }

    private fun saveNoteIfNotEmpty(){
        if(et_edit_title.text.isNotEmpty()) {
            saveNote(saveButtonUsed = true)
            wasSaved = true
            actionViewModel.setAction(NotesAction.SHOW_LIST)
        } else {
            // TODO fehlenden Titel eleganter lösen
            Toast.makeText(context, R.string.no_title, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveNote(saveButtonUsed: Boolean = false){
        if (canSaveNote()) {
            Log.v(TAG, "saving with button: $saveButtonUsed")
            saveInputInNote()
            if (isNewNote) {
                notesViewModel.insertNote(currentNote, saveButtonUsed)
            } else {
                notesViewModel.updateNote(currentNote, saveButtonUsed)
            }
        }
    }

    private fun canSaveNote() = (!wasSaved && et_edit_title.text.isNotEmpty())

    private fun saveInputInNote() {
        currentNote = if(isNewNote) {
            NoteEntity()
        } else {
            notesViewModel.selectedNote.value!!
        }
        storeTextData()
        storeEnumData()
    }

    private fun storeTextData(){
        currentNote.title = et_edit_title.text.toString()
        currentNote.content = et_edit_description.text.toString()
        currentNote.date = DateFormat.format("yyyy/MM/dd HH:mm:ss", Date().time).toString()
    }

    private fun storeEnumData(){
        if(tb_edit_priority.isChecked) {
            currentNote.setPriorityAsEnum(Priority.URGENT)
        } else {
            currentNote.setPriorityAsEnum(Priority.REMINDER)
        }
        if(tb_edit_protected.isChecked) {
            currentNote.setLockedAsEnum(Lock.LOCKED)
        } else {
            currentNote.setLockedAsEnum(Lock.UNLOCKED)
        }
        currentNote.setCategoryAsEnum(category)
    }

}