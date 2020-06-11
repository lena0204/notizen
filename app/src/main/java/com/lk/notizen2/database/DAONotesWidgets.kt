/*
 * Copyright (c) 2020 Lena Kociemba
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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