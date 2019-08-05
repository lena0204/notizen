package com.lk.notizen2.models

import com.lk.notizen2.R

/**
 * Erstellt von Lena am 06.10.18.
 */
data class Category(
    val id: Int = 0,
    var name: String = "",
    val color: Int = R.color.normal,
    var lineNumber: Int = 4
) {

    fun flatToString(): String {
        return "$id;$name;$color;$lineNumber"
    }

    companion object {

        fun parseFromString(flatCategory: String): Category {
            val attributes = flatCategory.split(";")
            return Category(attributes[0].toInt(), attributes[1],
                attributes[2].toInt(), attributes[3].toInt())
        }
    }

}