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

    @Insert
    fun insertNote(entity: NoteEntity): Long    // Long returns the row number inserted

    @Update
    fun updateNote(entity: NoteEntity)

    @Delete
    fun deleteNote(entity: NoteEntity)

    @Query("DELETE FROM notes WHERE note_id >= 0")
    fun deleteAll()

}