package com.lk.notizen2.models

import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.lk.notizen2.R

/**
 * Erstellt von Lena am 06.10.18.
 */
data class Category(
    val number: Int = 0,
    val color: Int = R.color.white,
    val colorDark: Int = R.color.black,
    val category: String = ""
) {
    companion object {
        fun createDrawableForColor(color: Int, resources: Resources): Drawable {
            return resources.getDrawable(color)
        }
    }
}