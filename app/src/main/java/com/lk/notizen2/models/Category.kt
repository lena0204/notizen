package com.lk.notizen2.models

import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.lk.notizen2.R

/**
 * Erstellt von Lena am 06.10.18.
 */
data class Category(
    var id: Int = 0,
    var name: String = "",
    val color: Int = R.color.normal,
    val lineNumber: Int = 4
)