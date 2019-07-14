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
            val notesList = NotesConversion().fromTableData(data)
            viewModel.insertAllNotes(notesList)
        }

    }
}