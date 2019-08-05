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