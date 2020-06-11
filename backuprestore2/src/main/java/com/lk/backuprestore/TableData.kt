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

package com.lk.backuprestore

/**
 * Erstellt von Lena am 2019-05-30.
 */
class TableData : Iterable<List<String>> {

    private val TAG = "TableData"

    private var data : MutableList<List<String>> = mutableListOf()

    fun addDataToList(row : List<String>) {
        data.add(row)
    }

    fun isEmpty() = data.isEmpty()

    override fun iterator(): Iterator<List<String>> = data.iterator()

    fun toCsvString() : String {
        val stringBuilder = StringBuilder()
        for(row in data) {
            stringBuilder.append("\"")
            for(cell in row) {
                stringBuilder.append(cell).append("\";\"")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            stringBuilder.deleteCharAt(stringBuilder.length - 1) // has been shortened by one
            stringBuilder.append("#")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()
    }

    companion object {
        fun fromCsvString(text: String): TableData {
            val tableData = TableData()
            val rows = text.split("\"#\"")
            for(row in rows) {
                val cells = row.split("\";\"").toMutableList()
                cells[0] = cells[0].trim('\"')
                cells[cells.lastIndex] = cells[cells.lastIndex].trim('\"')
                tableData.addDataToList(cells)
            }
            return tableData
        }
    }

}