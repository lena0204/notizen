package com.lk.notizen2.models

import android.app.Application
import android.renderscript.RenderScript
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

    init{
        filterPriority.value = Priority.ALL
        filterColor.value = Categories.ALL
        selectedNote.value = NoteEntity()
    }

    fun addListObservers(owner: LifecycleOwner, observer: Observer<Any>){
        filterPriority.observe(owner, observer)
        filterColor.observe(owner, observer)
        getNotes().observe(owner, observer)
    }

    fun setSelectedNoteFromId(id: Int){
        val note = getNotes().value?.find { note -> note.id == id }
        if(note != null) {
            selectedNote.value = note
            Log.v(TAG, "selected note changed")
        }
    }

    fun getNotes(): LiveData<List<NoteEntity>> = repository.getNotesList()

    fun insertNote(note: NoteEntity){
        repository.insertNote(note)
    }

    fun updateNote(note: NoteEntity){
        repository.updateNote(note)
    }

    fun deleteNoteFromId(noteId: Int){
        val note = getNotes().value?.find { note -> note.id == noteId }
        if(note != null)
            repository.deleteNote(note)
    }

    fun addTestNotes(){
        repository.deleteAllNotes()
        val eins = NoteEntity.newNote("Titel 1", "Text 214235khsfmnbcvcxklslkjfs",
            Priority.URGENT_LOCKED, Categories.PURPLE, "10.10.2018")
        val zwei = NoteEntity.newNote("Titel 2", "test 2 klsfdiwentew 1246543 kl23ljk",
            Priority.REMINDER, Categories.GREEN, "25.10.2018")
        insertNote(eins)
        insertNote(zwei)
        Log.d(TAG, "Added test notes")
    }

}