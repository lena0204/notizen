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
            rowList.add(note.id.toString())
            rowList.add(note.priority.toString())
            rowList.add(note.category.toString())
            rowList.add(note.locked.toString())
            rowList.add(note.title)
            rowList.add(note.content)
            rowList.add(note.date)
            tableData.addDataToList(rowList)
        }
        return tableData
    }

    override fun fromTableData(data: TableData): List<NoteEntity> {
        val notesList = mutableListOf<NoteEntity>()
        for (row in data) {
            val note = NoteEntity()
            note.priority = row[1].toInt()
            note.category = row[2].trim('\"').toInt()
            note.locked = row[3].trim('\"').toInt()
            note.title = row[4]
            note.content = row[5]
            note.date = row[6]
            notesList.add(note)
        }
        return notesList
    }

}