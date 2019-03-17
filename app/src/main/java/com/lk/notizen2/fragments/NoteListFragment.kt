package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.adapters.NotesAdapter
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.*
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteListFragment: Fragment(), Observer<Any>, NotesAdapter.OnClickListener {

    private val TAG = "NoteListFragment"

    private lateinit var fab: ImageButton
    private lateinit var rv: RecyclerView
    private var notesList: List<NoteEntity> = listOf()

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var actionViewModel: ActionViewModel
    private var deleteId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, args: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_main_button, container, false)
        fab = rootView.findViewById<View>(R.id.button_add) as ImageButton
        rv = rootView.findViewById<View>(R.id.recyclerview) as RecyclerView
        registerForContextMenu(rv)
        return rootView
    }

    override fun onActivityCreated(args: Bundle?) {
        super.onActivityCreated(args)
        initialiseViewModels()
        requireActivity().actionBar?.setTitle(R.string.app_name)
        fab.setOnClickListener {    // TESTING_ notwendig, dass für den FAB eine Attribut erzeugt wird??
            actionViewModel.setAction(NotesAction.NEW_NOTE)
            notesViewModel.selectedNote.value = NoteEntity()
        }
    }

    private fun initialiseViewModels(){
        notesViewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        actionViewModel = ViewModelFactory.getActionViewModel(requireActivity())
        notesViewModel.addListObservers(this, this)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    override fun onChanged(update: Any?) {
        if(update != null) {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView(){
        if (notesViewModel.getNotes().value != null) {
            setupDatalistAndAdapter(
                notesViewModel.getNotes().value!!,
                notesViewModel.filterColor.value!!,
                notesViewModel.filterPriority.value!!
            )
        }
    }

    private fun setupDatalistAndAdapter(notesData: List<NoteEntity>,
                                       filterCategory: Category,
                                       filterPriority: Priority) {
        notesList = notesData
        filterNotesList(filterCategory, filterPriority)
        setupRecyclerAdapter()
    }

    private fun filterNotesList(filterCategory: Category, filterPriority: Priority) {
        if (filterCategory != Categories.ALL) {
            notesList = notesList.filter { note -> note.category == filterCategory.number }
        }
        if (filterPriority != Priority.ALL) {
            notesList = notesList.filter { note -> note.getPriorityAsEnum() == filterPriority }
        }
    }

    private fun setupRecyclerAdapter(){
        val adapter = NotesAdapter(notesList, requireActivity())
        adapter.setListener(this)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = adapter
    }

    override fun onShowNote(noteId: Int) {
        Log.d(TAG, "showing note with id $noteId")
        notesViewModel.setSelectedNoteFromId(noteId)
        actionViewModel.setAction(NotesAction.SHOW_NOTE)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        try {
            deleteId = (rv.adapter as NotesAdapter).selectedNoteId
            if (item.itemId == R.id.menu_delete) {
                notesViewModel.deleteNoteFromId(deleteId)

                // TODO Delete-Dialog anzeigen
                // val delete = DeleteDialog()
                // delete.show(fragmentManager, "Delete")
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.localizedMessage + "; " + ex.message)
        }
        return super.onContextItemSelected(item)
    }

    // TODO Implementierung des Delete-Dialogs fehlt noch
    fun deleteItem() {
        notesViewModel.deleteNoteFromId(deleteId)
        Toast.makeText(activity, R.string.toast_deleted, Toast.LENGTH_SHORT).show()
    }
}