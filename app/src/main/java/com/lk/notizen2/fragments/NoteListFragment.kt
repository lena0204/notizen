package com.lk.notizen2.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lk.notizen2.R
import com.lk.notizen2.adapters.CardsAdapter
import com.lk.notizen2.adapters.NotesAdapter
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.models.Category
import com.lk.notizen2.models.NotesViewModel
import com.lk.notizen2.utils.Categories
import com.lk.notizen2.utils.Priority

/**
 * Erstellt von Lena am 06.10.18.
 */
class NoteListFragment: Fragment(), Observer<Any>, NotesAdapter.onClickListener {

    private val TAG = "NoteListFragment"
    private lateinit var fab: ImageButton
    private lateinit var rv: RecyclerView
    private lateinit var notesViewModel: NotesViewModel
    private var deleteId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, args: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_main_button, container, false)
        fab = rootView.findViewById<View>(R.id.button_add) as ImageButton
        rv = rootView.findViewById<View>(R.id.recyclerview) as RecyclerView
        // registerForContextMenu(rv)
        return rootView
    }

    override fun onActivityCreated(args: Bundle?) {
        super.onActivityCreated(args)
        notesViewModel = ViewModelProviders.of(requireActivity()).get(NotesViewModel::class.java)
        notesViewModel.addListObservers(this, this)
        requireActivity().actionBar!!.setTitle(R.string.app_name)
        // FloatingActionButton klickbar machen
        // TODO neue Notiz anlegen: Listener fab!!.setOnClickListener { listener!!.onNewTodo() }
    }

    private fun setupRecyclerView(notesData: List<NoteEntity>, filterCategory: Category?, filterPriority: Priority?) {
        if(filterCategory != null && filterPriority != null) {
            var dataset: List<NoteEntity> = notesData
            if (filterCategory != Categories.ALL) {
                dataset = dataset.filter { note -> note.category == filterCategory.number }
            }
            if (filterPriority != Priority.ALL) {
                dataset = dataset.filter { note -> note.getPriorityAsEnum() == filterPriority }
            }
            val adapter = NotesAdapter(dataset, requireActivity())
            adapter.setListener(this)
            rv.layoutManager = LinearLayoutManager(activity)
            rv.adapter = adapter
        } else {
            Log.e(TAG, "NullPointer: category: $filterCategory, priority: $filterPriority.")
        }
    }

    override fun onChanged(update: Any?) {
        if(update != null) {
            setupRecyclerViewWithLivedata()
        }
    }

    private fun setupRecyclerViewWithLivedata(){
        if (notesViewModel.getNotes().value != null) {
            setupRecyclerView(
                notesViewModel.getNotes().value!!,
                notesViewModel.filterColor.value,
                notesViewModel.filterPriority.value
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViewWithLivedata()
    }

    override fun onShowNote(noteId: Int) {
        Log.d(TAG, "showing note with id $noteId")
        notesViewModel.setSelectedNoteFromId(noteId)
    }

    /*override fun onContextItemSelected(item: MenuItem): Boolean {
        try {
            deleteId = (rv.adapter as NotesAdapter).selectedNoteId
            if (item.itemId == R.id.action_delete) {
                // die Notiz löschen; Dialog aufrufen um löschen zu bestätigen
                val delete = DeleteDialog()
                delete.show(fragmentManager, "Delete")
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.localizedMessage + "; " + ex.message)
        }
        return super.onContextItemSelected(item)
    }

    fun deleteItem() {
        notesViewModel.deleteNoteFromId(deleteId)
        Toast.makeText(activity, R.string.toast_deleted, Toast.LENGTH_SHORT).show()
    }*/
}