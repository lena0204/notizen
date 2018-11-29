package com.lk.notizen2.database

import androidx.annotation.NonNull
import androidx.room.*

/**
 * Erstellt von Lena am 06.10.18.
 */
@Entity(tableName = "widgets")
class WidgetEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "widget_id")
    var id: Int = 0

    var note_id: Int = 0

    @ColumnInfo(name = "widget_type")
    var type: Int = 0

    var show_priority: Boolean = true

    var show_content: Boolean = true
}