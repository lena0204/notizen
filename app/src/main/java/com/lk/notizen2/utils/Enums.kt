package com.lk.notizen2.utils

import com.lk.notizen2.R
import com.lk.notizen2.models.Category

/**
 * Erstellt von Lena am 23.09.18.
 */
object Categories{

    val WHITE = Category(category = "Weiss")
    val YELLOW = Category(1, R.color.yellow, R.color.yellowD, "Gelb")
    val ORANGE = Category(2, R.color.orange, R.color.orangeD, "Orange")
    val RED = Category(3, R.color.red, R.color.redD, "Rot")
    val PURPLE = Category(4, R.color.purple, R.color.purpleD, "Lila")
    val BLUE = Category(5, R.color.blue, R.color.blueD, "Blau")
    val GREEN = Category(6, R.color.green, R.color.greenD, "Gruen")
    val ALL = Category(7, category = "All")

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

}

enum class Priority {
    URGENT,
    URGENT_LOCKED,
    REMINDER,
    REMINDER_LOCKED,
    ALL
}