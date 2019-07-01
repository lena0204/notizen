package com.lk.notizen2.utils

import com.lk.notizen2.R
import com.lk.notizen2.models.Category

/**
 * Erstellt von Lena am 23.09.18.
 */
object Categories {

    // TODO Farbnamen müssen auch sprachenspezifisch sein ...

    val WHITE = Category(name = "Weiss")
    val YELLOW = Category(1, "Gelb", R.color.yellow)
    val ORANGE = Category(2, "Orange", R.color.orange)
    val RED = Category(3, "Rot", R.color.red)
    val PURPLE = Category(4, "Lila", R.color.purple)
    val BLUE = Category(5, "Blau", R.color.blue)
    val GREEN = Category(6, "Gruen", R.color.green)
    val ALL = Category(7, "Alle")

    fun getCategory(counter: Int): Category{
        return when(counter){
            0 -> WHITE
            1 -> YELLOW
            2 -> ORANGE
            3 -> RED
            4 -> PURPLE
            5 -> BLUE
            6 -> GREEN
            else -> ALL
        }
    }

    fun getCategoryArray(): Array<String> {
        return arrayOf(WHITE.name, YELLOW.name,
            ORANGE.name, RED.name, PURPLE.name, BLUE.name, GREEN.name, ALL.name)
    }
}

enum class NavigationActions {
    NEW_NOTE,
    SHOW_NOTE,
    DELETE_NOTE,
    EDIT_NOTE,
    SAVE_NOTE,
    SHOW_PREFERENCES,
    SHOW_LIST
}

enum class Design {
    THEME_LIGHT,
    THEME_DARK,
    THEME_BLACK
}