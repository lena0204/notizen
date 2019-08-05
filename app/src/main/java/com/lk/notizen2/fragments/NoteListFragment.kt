package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
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
import kotlinx.android.synthetic.main.activity_main_button.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteListFragment: Fragment(), Observer<Any>, NotesAdapter.OnClickListener {

    private val TAG = "NoteListFragment"

    private var notesList: List<NoteEntity> = listOf()
    private lateinit var rv: RecyclerView
    private lateinit var viewModel: NotesViewModel
    private var deleteId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, args: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_main_button, container, false)
        rv = rootView.findViewById<View>(R.id.recyclerview) as RecyclerView
        registerForContextMenu(rv)
        return rootView
    }

    override fun onActivityCreated(args: Bundle?) {
        super.onActivityCreated(args)
        initialiseViewModels()
        requireActivity().actionBar?.setTitle(R.string.app_name)
        button_add.setOnClickListener {
            viewModel.doAction(NavigationActions.NEW_NOTE, NoteEntity())
        }
    }

    private fun initialiseViewModels(){
        viewModel = ViewModelFactory.getNotesViewModel(requireActivity())
        viewModel.observeListAndActions(this, this)
    }

    override fun onResume() {
        super.onResume()
        this.setupRecyclerView()
    }

    override fun onChanged(update: Any?) {
        if(update != null) {
            this.setupRecyclerView()
        }
    }

    private fun setupRecyclerView(){
        notesList = viewModel.getFilteredNotes()
        val adapter = NotesAdapter(notesList)
        adapter.setListener(this)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = adapter
    }

    override fun onShowNote(noteId: Int) {
        Log.v(TAG, "Showing note with id $noteId")
        viewModel.doAction(NavigationActions.SHOW_NOTE, noteId)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        try {
            deleteId = (recyclerview.adapter as NotesAdapter).selectedNoteId
            Log.d(TAG, "To be deleted: $deleteId")
            if (item.itemId == R.id.menu_delete) {
                viewModel.doAction(NavigationActions.DELETE_NOTE, deleteId)
                Toast.makeText(activity, R.string.toast_deleted, Toast.LENGTH_SHORT).show()

                // IDEA_ Delete-Dialog anzeigen
                // val delete = DeleteDialog()
                // delete.show(fragmentManager, "Delete")
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.localizedMessage + "; " + ex.message)
        }
        return super.onContextItemSelected(item)
    }
}