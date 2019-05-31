package com.lk.notizen2.utils

import com.lk.notizen2.R
import com.lk.notizen2.models.Category

/**
 * Erstellt von Lena am 23.09.18.
 */
object Categories {

    // TODO Farbnamen müssen auch sprachenspezifisch sein ...

    val WHITE = Category(category = "Weiss")
    val YELLOW = Category(1, R.color.yellow, "Gelb")
    val ORANGE = Category(2, R.color.orange, "Orange")
    val RED = Category(3, R.color.red, "Rot")
    val PURPLE = Category(4, R.color.purple, "Lila")
    val BLUE = Category(5, R.color.blue, "Blau")
    val GREEN = Category(6, R.color.green, "Gruen")
    val ALL = Category(7, category = "Alle")

    fun getCategory(counter: Int): Category{
        return when(counter){
            1 -> YELLOW
            2 -> ORANGE
            3 -> RED
            4 -> PURPLE
            5 -> BLUE
            6 -> GREEN
            else -> WHITE
        }
    }

    fun getCategoryArray(): Array<String> {
        return arrayOf(WHITE.category, YELLOW.category,
            ORANGE.category, RED.category, PURPLE.category, BLUE.category, GREEN.category, ALL.category)
    }

}

enum class Priority {
    REMINDER,
    URGENT,
    ALL
}

enum class Lock {
    UNLOCKED,
    LOCKED,
    ALL
}

enum class NotesAction {
    NONE,
    NEW_NOTE,
    SHOW_NOTE,
    CHECK_PROTECTION,
    EDIT_NOTE,
    SHOW_PREFERENCES,
    SHOW_LIST
}

enum class Design {
    THEME_LIGHT,
    THEME_DARK,
    THEME_BLACK
}