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

    @ColumnInfo(name = "note_locked")
    var locked: Int = 0

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

    fun getLockedAsEnum(): Lock = Lock.values()[locked]

    fun setLockedAsEnum(_protected: Lock){
        locked = _protected.ordinal
    }

    override fun toString(): String {
        return "{id: $id, title: $title, category: $category, priority: $priority, locked: $locked, date: $date }"
    }

    fun toLimitedString(): String {
        return "{id=$id, Priority = $priority, Locked = $locked}"
    }
}