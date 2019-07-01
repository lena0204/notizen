package com.lk.backuprestore

import android.util.Log

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
                val cells = row.split("\";\"")
                tableData.addDataToList(cells)
            }
            return tableData
        }
    }

}