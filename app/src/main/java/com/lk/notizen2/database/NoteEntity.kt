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

    @ColumnInfo(name = "note_protectedValue")
    var protectedValue: Int = 0

    @ColumnInfo(name = "note_archived")
    var archived: Boolean = false

    @ColumnInfo(name = "note_date")
    var date: String = ""

    fun setProtected(value: Boolean) {
        protectedValue = if(value) {
            1
        } else {
            0
        }
    }

    fun getCategoryAsEnum(): Category = Categories.getCategory(category)

    fun setCategoryAsEnum(_category: Category){
        category = _category.id
    }

    fun isProtected(): Boolean = protectedValue == 1

    fun isEmpty(): Boolean = id == 0 && title == ""

    override fun toString(): String {
        return "{id: $id, title: $title, category: $category, protected: ${isProtected()}, archived: $archived date: $date }"
    }

    fun toLimitedString(): String {
        return "{id=$id, Category = $category, Archived = $archived, Protected = ${isProtected()}}"
    }
}