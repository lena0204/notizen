package com.lk.notizen2.database

import androidx.annotation.NonNull
import androidx.room.*
import com.lk.notizen2.models.Category
import com.lk.notizen2.utils.*

/**
 * Erstellt von Lena am 06.10.18.
 */
@Entity(tableName = "notes")
class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    var id: Int = 0

    @ColumnInfo(name = "note_title")
    @NonNull
    var title: String = ""

    @ColumnInfo(name = "note_content")
    @NonNull
    var content: String = ""

    @ColumnInfo(name = "note_category")
    var category: Int = 0

    @ColumnInfo(name = "note_priority")
    var priority: Int = 0

    @ColumnInfo(name = "note_date")
    var date: String = ""

    fun getCategoryAsEnum(): Category = Categories.getCategory(category)

    fun setCategoryAsEnum(_category: Category){
        category = _category.number
    }

    fun getPriorityAsEnum(): Priority = Priority.values()[priority]

    fun setPriorityAsEnum(_priority: Priority){
        priority = _priority.ordinal
    }

    companion object {
        fun newNote(_title: String, _content: String, _priority: Priority, _category: Category,
                    _date: String): NoteEntity
        {
            val note = NoteEntity()
            note.title = _title
            note.content = _content
            note.date = _date
            note.setCategoryAsEnum(_category)
            note.setPriorityAsEnum(_priority)
            return note
        }
    }
}