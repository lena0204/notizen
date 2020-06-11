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
 * Erstellt von Lena am 2019-07-31.
 */
class IntArrayConversionHelper: Conversion<IntArray>() {

    override fun toTableData(data: List<IntArray>): TableData {
        var convertedRow: MutableList<String>
        val tableData = TableData()
        for(row in data) {
            convertedRow = mutableListOf()
            for(number in row) {
                convertedRow.add(number.toString())
            }
            tableData.addDataToList(convertedRow.toList())
        }
        return tableData
    }

    override fun fromTableData(data: TableData): List<IntArray> {
        var convertedRow: MutableList<Int>
        val dataList = mutableListOf<IntArray>()
        for(row in data) {
            convertedRow = mutableListOf()
            for(text in row) {
                convertedRow.add(text.toInt())
            }
            dataList.add(convertedRow.toIntArray())
        }
        return dataList
    }

}