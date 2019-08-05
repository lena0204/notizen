package com.lk.notizen2.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Erstellt von Lena am 06.10.18.
 */
@Database(entities = [NoteEntity::class], version = 6, exportSchema = true)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun notesDao(): DAONotesWidgets

    companion object {

        @Volatile private var INSTANCE: NotesDatabase? = null

        private val migration_5_6 = object : Migration(5,6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE widgets")
            }
        }

        fun getInstance(context: Context): NotesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                NotesDatabase::class.java, "notes.db")
                .addMigrations(migration_5_6)
                .build()
    }
}