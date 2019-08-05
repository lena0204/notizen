package com.lk.backuprestore

import org.junit.Test
import org.junit.Assert.*

/**
 * Erstellt von Lena am 2019-07-31.
 */
class ParsingTest {

    @Test
    fun testParsingData() {
        // when
        val testData = getTestData()
        val conversionHelper = IntArrayConversionHelper()

        // then
        val csvString = conversionHelper.toTableData(testData).toCsvString()
        println(csvString)
        val resultData = conversionHelper.fromTableData(TableData.fromCsvString(csvString))

        // expect
        assertArrayEquals(testData[0], resultData[0])
        assertArrayEquals(testData[1], resultData[1])

    }

    private fun getTestData(): List<IntArray> {
        val first: IntArray = intArrayOf(3, 5, 1, 6)
        val second: IntArray = intArrayOf(4, 6, 1, 10)
        return listOf(first, second)
    }
}