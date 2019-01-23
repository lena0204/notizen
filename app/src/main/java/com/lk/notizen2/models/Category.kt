package com.lk.notizen2.models

import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.lk.notizen2.R

/**
 * Erstellt von Lena am 06.10.18.
 */
data class Category(
    val number: Int = 0,
    val color: Int = R.color.normal,
    val category: String = "",
    val lineNumber: Int = 2
) {
    companion object {
        fun createDrawableForColor(color: Int, resources: Resources): Drawable {
            return resources.getDrawable(color)
        }
    }
}