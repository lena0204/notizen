package com.lk.notizen2.database

import android.content.Context
import androidx.room.*

/**
 * Erstellt von Lena am 06.10.18.
 */
@Database(entities = [NoteEntity::class, WidgetEntity::class], version = 2, exportSchema = true)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): DAONotesWidgets

    companion object {

        @Volatile private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                NotesDatabase::class.java, "notes.db")
                .build()
    }
}