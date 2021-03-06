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

package com.lk.notizen2.main

import com.lk.backuprestore.Conversion
import com.lk.backuprestore.TableData
import com.lk.notizen2.database.NoteEntity

/**
 * Erstellt von Lena am 2019-05-31.
 */
class NotesConversion : Conversion<NoteEntity>() {

    override fun toTableData(data: List<NoteEntity>): TableData {
        val tableData = TableData()
        val rowList: MutableList<String> = mutableListOf()
        for (note in data) {
            rowList.clear()
            rowList.add(note.id.toString())
            rowList.add(note.category.toString())
            rowList.add(note.isProtected().toString())
            rowList.add(note.archived.toString())
            rowList.add(note.title)
            rowList.add(note.content)
            rowList.add(note.date)
            tableData.addDataToList(rowList.toList())
            // toList() notwendig um die Übergabe einer Referenz auf ein weitergenutztes Objekt zu verhindern
        }
        return tableData
    }

    // TODO handle errors in csv gracely, at least without shutdown; might they occur?
    override fun fromTableData(data: TableData): List<NoteEntity> {
        val notesList = mutableListOf<NoteEntity>()
        for (row in data) {
            val note = NoteEntity()
            // note.id = row[0].trim('\"').toInt() -> Autoincrement der DB nutzens
            note.category = row[1].toInt()
            note.setProtected(row[2].toBoolean())
            note.archived = row[3].toBoolean()
            note.title = row[4]
            note.content = row[5]
            note.date = row[6]
            notesList.add(note)
        }
        return notesList
    }

}