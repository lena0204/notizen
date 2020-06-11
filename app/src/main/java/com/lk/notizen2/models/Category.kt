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