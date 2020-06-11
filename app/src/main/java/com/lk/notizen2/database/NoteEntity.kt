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