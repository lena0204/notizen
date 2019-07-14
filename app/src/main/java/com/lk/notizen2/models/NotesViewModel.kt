package com.lk.notizen2.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.database.NotesRepository
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "NotesViewModel"

    private val repository = NotesRepository(application)

    // TODO make them private with getters / setters

    val selectedNote = MutableLiveData<NoteEntity>()
    val selectedCategory = MutableLiveData<Category>()

    val filteredNotes = MediatorLiveData<List<NoteEntity>>()
    val filterCategories = MutableLiveData<List<Category>>()

    private val notes = repository.getNotesList()

    private val notesAction = MutableLiveData<NavigationActions>()

    init {
        filterCategories.value = listOf(Categories.ALL)
        selectedCategory.value = Categories.ALL
        selectedNote.value = NoteEntity()
        notesAction.value = NavigationActions.SHOW_LIST

        filteredNotes.addSource(filterCategories) {
            filteredNotes.value = filterNotes(it)
        }
        filteredNotes.addSource(notes) {
            filteredNotes.value = filterNotes(filterCategories.value!!)
        }
    }

    fun observeListAndActions(owner: LifecycleOwner, observer: Observer<Any>){
        filteredNotes.observe(owner, observer)
        notesAction.observe(owner, observer)
    }

    private fun filterNotes(categories: List<Category>): List<NoteEntity> {
        Log.v(TAG, "filterNotes: ${categories}")
        return when {
            notes.value == null -> listOf()
            categories[0] == Categories.ALL -> notes.value!!
            else -> {
                notes.value!!.filter {
                    categories.contains(Categories.getCategory(it.category))
                }
            }
        }
    }

    fun doAction(action: NavigationActions, noteID: Int) {
        val note = findNoteById(noteID)
        doAction(action, note)
    }

    fun doAction(action: NavigationActions, note: NoteEntity) {
        when(action) {
            NavigationActions.SHOW_LIST,
            NavigationActions.NEW_NOTE -> {
                selectedNote.value = NoteEntity()
                selectedCategory.value = Categories.WHITE
            }
            NavigationActions.SHOW_NOTE,
                NavigationActions.EDIT_NOTE -> selectedNote.value = note
            NavigationActions.SHOW_PREFERENCES -> {}
            NavigationActions.SAVE_NOTE -> saveNote(note)
            NavigationActions.DELETE_NOTE -> deleteNoteFromId(note)
        }
        notesAction.value = action
    }

    private fun findNoteById(id: Int): NoteEntity {
        return filteredNotes.value?.find { note -> note.id == id } ?: NoteEntity()
    }

    private fun saveNote(note: NoteEntity) {
        val oldNote = findNoteById(note.id)
        if(oldNote.isEmpty()) {
            repository.insertNote(note)
        } else {
            repository.updateNote(note)
        }
        selectedNote.value = note   // PROBLEM_ Die ID fehlt und somit wird die Notiz erneut eingetragen
        Log.d(TAG, selectedNote.value?.toLimitedString())
    }

    private fun deleteNoteFromId(note: NoteEntity) {
        repository.deleteNote(note)
    }

    // backup & restore
    fun deleteAllNotes() {
        Log.d(TAG, "Will delete ALL notes!")
        repository.deleteAllNotes()
    }

    fun insertAllNotes(notes: List<NoteEntity>) {
        for(note in notes) {
            repository.insertNote(note)
        }
    }
}