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

    private lateinit var viewModel: NotesViewModel

    private var category = Categories.WHITE
    private var currentNote = NoteEntity()
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
        viewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        viewModel.observeSelectedCategory(this, this)
        viewModel.observeSelectedNote(this, this)
    }
    private fun setClickListeners(){
        bt_edit_save.setOnClickListener {
            onSave()
        }
        bt_edit_category.setOnClickListener {
            onShowCategoryDialog()
        }
    }

    private fun onShowCategoryDialog(){
        requireActivity().supportFragmentManager.transaction {
            add(CategoryDialog(), "CAT_DIALOG")
        }
    }

    // TODO nach dem Speichern taucht kurz die default version (Farbe und Buttons) auf -> verhindern
    override fun onChanged(update: Any?) {
        when {
            update != null && update is NoteEntity -> {
                onUpdateNote(update)
            }
            update != null && update is Category -> {
                onCategoryChosen(update)
            }
        }
    }

    private fun onCategoryChosen(newCategory: Category) {
        category = newCategory
        iv_edit_category.setImageResource(newCategory.color)
        bt_edit_category.imageTintList =
            ColorStateList.valueOf(requireActivity().resources.getColor(newCategory.color))
        bt_edit_category.imageTintMode = PorterDuff.Mode.SRC_ATOP
    }

    private fun onUpdateNote(updatedNote: NoteEntity?){
        if (updatedNote != null && !updatedNote.isEmpty()) {
            currentNote = updatedNote
            printNote(updatedNote)
        } else {
            currentNote = NoteEntity()
            printDefaultNote()
        }
    }

    private fun printNote(note: NoteEntity){
        tv_edit_id.text = note.id.toString()
        et_edit_title.setText(note.title, TextView.BufferType.EDITABLE)
        et_edit_description.setText(note.content,TextView.BufferType.EDITABLE)
        tb_edit_protected.isChecked = note.isProtected()
        viewModel.setSelectedCategory(note.getCategoryAsEnum())
    }

    private fun printDefaultNote(){
        tb_edit_protected.isChecked = false
        onCategoryChosen(Categories.WHITE)
    }

    private fun onSave(){
        if(et_edit_title.text.isNotEmpty()) {
            saveNote()
            wasSaved = true
            viewModel.doAction(NavigationActions.SHOW_LIST, -1)
        } else {
            // TODO fehlenden Titel eleganter lösen
            Toast.makeText(context, R.string.no_title, Toast.LENGTH_LONG).show()
        }
    }

    private fun saveNote(){
        if (canSaveNote()) {
            // TODO InstanceState vernünftig implementieren: Fragment merken und aktuellen Stand
            storeInputData()
            viewModel.doAction(NavigationActions.SAVE_NOTE, currentNote)
        }
    }
    private fun canSaveNote() = (!wasSaved && et_edit_title.text.isNotEmpty())

    private fun storeInputData() {
        currentNote.title = et_edit_title.text.toString()
        currentNote.content = et_edit_description.text.toString()
        currentNote.date = DateFormat.format("yyyy/MM/dd HH:mm:ss", Date().time).toString()
        currentNote.setProtected(tb_edit_protected.isChecked)
        currentNote.setCategoryAsEnum(category)
    }

    override fun onPause(){
        super.onPause()
        Log.v(TAG,"onPause: don't save atm")
        // saveNote()
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG,"onResume")
        // val note = viewModel.selectedNote.value
        // wasSaved = false
        // onUpdateNote(note)
    }

}