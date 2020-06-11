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

import android.os.Environment
import android.util.Log
import com.lk.backuprestore.listener.*
import kotlinx.coroutines.*
import java.io.*

/**
 * Erstellt von Lena am 2019-05-30.
 */
internal class FileAccess {

    private val TAG = this::class.java.simpleName

    fun writeToFile(data: TableData, listenerWrite: OnBackupFinished) {
        GlobalScope.launch(Dispatchers.Main) {
            val text = data.toCsvString()
            try {
                writeTextToFile(text)
                listenerWrite.onBackupFinished(Result.RESULT_SUCCESS)
            } catch (ex: IOException) {
                listenerWrite.onBackupFinished(Result.RESULT_FAILED)
            }
        }
    }

    private fun writeTextToFile(csvText: String){
        val internalStorage = Environment.getExternalStorageDirectory()
        if(internalStorage.canWrite()){
            val doc = File(internalStorage, BackupRestore.PATH)
            writeFile(doc, csvText)
        } else {
            Log.v(TAG, "SD-Karte kann nicht beschrieben werden.")
            throw IOException("SD-Karte kann nicht beschrieben werden.")
        }
    }

    private fun writeFile(file: File, text: String) {
        val writer = FileWriter(file)
        writer.write(text)
        writer.close()
    }

    fun readFromFile(listenerRead: OnRestoreFinished) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val text = readDataFromFile()
                val tableData = TableData.fromCsvString(text)
                listenerRead.onRestoreFinished(tableData)
            } catch (ex: IOException) {
                listenerRead.onRestoreFinished(TableData())
            }
        }
    }

    private fun readDataFromFile(): String {
        val sdcard = Environment.getExternalStorageDirectory()
        if(sdcard.canRead()){
            val doc = File(sdcard, BackupRestore.PATH)
            return readFile(doc)
        } else {
            Log.v(TAG, "SD-Karte kann nicht gelesen werden.")
            throw IOException("SD-Karte kann nicht gelesen werden.")
        }
    }

    private fun readFile(file: File): String {
        val reader = FileReader(file)
        val text = reader.readText()
        reader.close()
        return text
    }

}