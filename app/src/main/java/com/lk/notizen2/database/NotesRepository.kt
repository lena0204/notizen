package com.lk.notizen2.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

/**
 * Erstellt von Lena am 06.10.18.
 */
class NotesRepository(application: Application) {

    private val TAG = "NotesRepository"

    private val db = NotesDatabase.getInstance(application)
    private val dao = db.notesDao()
    private val notes: LiveData<List<NoteEntity>> = dao.getNotes()

    fun getNotesList(): LiveData<List<NoteEntity>> {
        Log.d(TAG, "DB row count: ${notes.value?.size}")
        return notes
    }

    fun insertNote(note: NoteEntity){
        GlobalScope.launch(Dispatchers.Default) {
            val nr = dao.insertNote(note)
            Log.d(TAG, "insert Value an $nr: $note")
        }
    }

    fun updateNote(note: NoteEntity){
        GlobalScope.launch(Dispatchers.Default) {
            dao.updateNote(note)
            Log.d(TAG, "update Value: $note")
        }
    }

    fun deleteNote(note: NoteEntity){
        GlobalScope.launch(Dispatchers.Default) {
            dao.deleteNote(note)
            Log.d(TAG, "delete note")
        }
    }

    fun deleteAllNotes(){
        GlobalScope.launch(Dispatchers.Default){
            dao.deleteAll()
        }
    }


}