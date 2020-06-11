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

package com.lk.notizen2.main

import com.lk.backuprestore.BackupRestore
import com.lk.backuprestore.TableData
import com.lk.backuprestore.listener.OnBackupFinished
import com.lk.backuprestore.listener.OnRestoreFinished
import com.lk.notizen2.models.NotesViewModel

/**
 * Erstellt von Lena am 13/03/2019.
 */
class BackupRestoreWrapper(private val viewModel: NotesViewModel) : OnRestoreFinished {

    private val TAG = "BackupRestoreWrapper"

    fun backupNotes(listener: OnBackupFinished) {
        val notes = viewModel.getFilteredNotes()
        if(notes.isNotEmpty()) {
            val tableData = NotesConversion().toTableData(notes)
            BackupRestore().backupData(tableData, listener)
        } else {
            // IDEA_ Fehler schmeißen, damit klar wird, dass keine Notizen zum sichern da sind?
        }
    }

    fun restoreNotes() {
        BackupRestore().restoreData(this)
    }

    override fun onRestoreFinished(data: TableData) {
        if(!data.isEmpty()) {
            viewModel.deleteAllNotes()
            val notesList = NotesConversion().fromTableData(data)
            /* TODO Problem: delete löscht teilweise noch eingefügte wieder mit weil die gleichzeitig laufen;
                evtl weitere Methode im Repo, damit beides im gleichen Scope asynchron laufen kann */
            Thread.sleep(50)
            viewModel.insertAllNotes(notesList)
        }

    }
}