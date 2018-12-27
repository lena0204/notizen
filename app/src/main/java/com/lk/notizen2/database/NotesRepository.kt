package com.lk.notizen2.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.experimental.launch

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesRepository(application: Application) {

    private val TAG = "NotesRepository"

    private val db = NotesDatabase.getInstance(application)
    private val dao = db.notesDao()
    private val notes: LiveData<List<NoteEntity>> = dao.getNotes()

    fun getNotesList(): LiveData<List<NoteEntity>> {
        return notes
    }

    fun insertNote(note: NoteEntity){
        launch {
            dao.insertNote(note)
            Log.d(TAG, "insert Value: " + note.toString())
        }
    }

    fun updateNote(note: NoteEntity){
        launch {
            dao.updateNote(note)
            Log.d(TAG, "update Value: " + note.toString())
        }
    }

    fun deleteNote(note: NoteEntity){
        launch {
            dao.deleteNote(note)
        }
    }

    fun deleteAllNotes(){
        launch{
            dao.deleteAll()
        }
    }


}