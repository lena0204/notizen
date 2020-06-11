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