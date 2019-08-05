package com.lk.backuprestore

import com.lk.backuprestore.listener.*

/**
 * Erstellt von Lena am 13/03/2019.
 */
class BackupRestore {

    private val TAG = this::class.java.simpleName

    // IDEA_ FileAccess auslagern
    fun backupData(tableData: TableData, listenerBackup: OnBackupFinished) {
        FileAccess().writeToFile(tableData, listenerBackup)
    }

    fun restoreData(listenerRestore: OnRestoreFinished) {
        FileAccess().readFromFile(listenerRestore)
    }

    companion object {
        const val PATH = "//NotesBackup.txt"
    }
}