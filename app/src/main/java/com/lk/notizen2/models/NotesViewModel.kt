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

    val selectedNote = MutableLiveData<NoteEntity>()
    val filterPriority = MutableLiveData<Priority>()
    val filterColor = MutableLiveData<Category>()
    val selectedCategory = MutableLiveData<Category>()

    init{
        filterPriority.value = Priority.ALL
        filterColor.value = Categories.ALL
        selectedCategory.value = Categories.ALL
        selectedNote.value = NoteEntity()
    }

    fun addListObservers(owner: LifecycleOwner, observer: Observer<Any>){
        filterPriority.observe(owner, observer)
        filterColor.observe(owner, observer)
        getNotes().observe(owner, observer)
    }

    fun getNotes(): LiveData<List<NoteEntity>> {
        return repository.getNotesList()
    }

    fun setSelectedNoteFromId(id: Int){
        val note = getNotes().value?.find { note -> note.id == id }
        if(note != null) {
            selectedNote.value = note
            Log.v(TAG, "selected note changed")
        }
    }

    fun insertNote(note: NoteEntity, savedButton: Boolean = false){
        repository.insertNote(note)
        selectedNote.value = note
        if (savedButton)
            selectedNote.value = NoteEntity()
    }

    fun updateNote(note: NoteEntity, savedButton: Boolean = false){
        repository.updateNote(note)
        selectedNote.value = note
        if (savedButton)
            selectedNote.value = NoteEntity()
        Log.d(TAG, selectedNote.value?.toString())
    }

    fun deleteNoteFromId(noteId: Int){
        val note = getNotes().value?.find { note -> note.id == noteId }
        if(note != null) {
            repository.deleteNote(note)
            selectedNote.value = NoteEntity()
        }
    }
}