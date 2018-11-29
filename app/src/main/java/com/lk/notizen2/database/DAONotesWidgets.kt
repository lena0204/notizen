package com.lk.notizen2.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Erstellt von Lena am 06.10.18.
 */
@Dao
interface DAONotesWidgets {

    @Query("SELECT * FROM notes")
    abstract fun getNotes(): LiveData<List<NoteEntity>>

    //@Query("SELECT * FROM notes WHERE note_id == :id")
    //abstract fun getNoteFromId(id: Int): NoteEntity

    @Insert
    abstract fun insertNote(entity: NoteEntity)

    @Update
    abstract fun updateNote(entity: NoteEntity)

    @Delete
    abstract fun deleteNote(entity: NoteEntity)

    @Query("DELETE FROM notes WHERE note_id >= 0")
    abstract fun deleteAll()

    @Query("SELECT * FROM widgets")
    abstract fun getWidget(): LiveData<List<WidgetEntity>>

    @Query("SELECT * FROM widgets WHERE widget_id == :id")
    abstract fun getWidgetFromId(id: Int): WidgetEntity

    @Query("SELECT * FROM widgets WHERE note_id == :noteId")
    abstract fun getWidgetFromNoteId(noteId: Int): WidgetEntity

    @Insert
    abstract fun insertWidget(entity: WidgetEntity)

    @Update
    abstract fun updateWidget(entity: WidgetEntity)

    @Delete
    abstract fun deleteWidget(entity: WidgetEntity)
}