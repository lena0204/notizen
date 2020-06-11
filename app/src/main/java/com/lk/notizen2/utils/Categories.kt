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

    fun getCategory(counter: Int): Category {
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

    fun getAllCategories() : List<Category> {
        return listOf(WHITE, YELLOW, ORANGE, RED, PURPLE, BLUE, GREEN, ALL)
    }

    fun getCategoryNameArray(): Array<String> {
        return arrayOf(WHITE.name, YELLOW.name,
            ORANGE.name, RED.name, PURPLE.name, BLUE.name, GREEN.name, ALL.name)
    }

    fun transformToCategoryList(set: Set<String>): List<Category> {
        val valueList = mutableListOf<Category>()
        set.forEach {
            valueList.add(getCategory(it.toInt()))
        }
        return valueList
    }

    fun readCurrentDataFromPreferences(spw: SharedPrefWrapper) {
        val categoriesSet = spw.readSet(Constants.PREF_PERSONALISATION, setOf())
        for(categoryString in categoriesSet) {
            val prefCategory = Category.parseFromString(categoryString)
            val currentCategory = getCategory(prefCategory.id)
            currentCategory.name = prefCategory.name
            currentCategory.lineNumber = prefCategory.lineNumber
        }
    }
}