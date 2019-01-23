package com.lk.notizen2.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Erstellt von Lena am 06.10.18.
 */
@Dao
interface DAONotesWidgets {

    @Query("SELECT * FROM notes ORDER BY note_date DESC")
    fun getNotes(): LiveData<List<NoteEntity>>

    //@Query("SELECT * FROM notes WHERE note_id == :id")
    //abstract fun getNoteFromId(id: Int): NoteEntity

    @Insert
    fun insertNote(entity: NoteEntity)

    @Update
    fun updateNote(entity: NoteEntity)

    @Delete
    fun deleteNote(entity: NoteEntity)

    @Query("DELETE FROM notes WHERE note_id >= 0")
    fun deleteAll()

    @Query("SELECT * FROM widgets")
    fun getWidget(): LiveData<List<WidgetEntity>>

    @Query("SELECT * FROM widgets WHERE widget_id == :id")
    fun getWidgetFromId(id: Int): WidgetEntity

    @Query("SELECT * FROM widgets WHERE note_id == :noteId")
    fun getWidgetFromNoteId(noteId: Int): WidgetEntity

    @Insert
    fun insertWidget(entity: WidgetEntity)

    @Update
    fun updateWidget(entity: WidgetEntity)

    @Delete
    fun deleteWidget(entity: WidgetEntity)

}