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

package com.lk.notizen2

import com.lk.backuprestore.TableData
import com.lk.notizen2.database.NoteEntity
import com.lk.notizen2.main.NotesConversion
import com.lk.notizen2.utils.Categories
import org.junit.Assert
import org.junit.Test

/**
 * Erstellt von Lena am 2019-06-25.
 */
class BackupFormatTest {

    private val TAG = "BackupFormatTest"

    @Test
    fun testParsing() {
        val note1 = firstNote()
        val note2 = secondNote()

        val notesWrite = listOf(note1, note2)
        var tableData = NotesConversion().toTableData(notesWrite)
        val text = tableData.toCsvString()
        println("$TAG: \tCsv-Ausgabe $text")
        Assert.assertTrue(text.isNotEmpty())
        tableData = TableData.fromCsvString(text)
        val notesRead = NotesConversion().fromTableData(tableData)
        Assert.assertTrue(notesRead.isNotEmpty())
        Assert.assertEquals(notesWrite[0].title, notesRead[0].title)
    }

    private fun firstNote(): NoteEntity {
        val note1 = NoteEntity()
        note1.id = 1
        note1.date = "06.06.2019"
        note1.setCategoryAsEnum(Categories.ORANGE)
        note1.title = "Titel 1"
        note1.content = "This is a test ..."
        note1.setProtected(true)
        return note1
    }

    private fun secondNote(): NoteEntity {
        val note2 = NoteEntity()
        note2.id = 3
        note2.date = "06.08.2019"
        note2.setCategoryAsEnum(Categories.PURPLE)
        note2.title = "Titel 2"
        note2.content = "This is a test 2 ..."
        note2.setProtected(false)
        return note2
    }

}