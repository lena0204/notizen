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

    internal fun toCsvString() : String {
        val stringBuilder = StringBuilder()
        for(row in data) {
            stringBuilder.append("\"")
            for(cell in row) {
                stringBuilder.append(cell).append("\";\"")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            stringBuilder.deleteCharAt(stringBuilder.length - 2)
            stringBuilder.append("#")
        }
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