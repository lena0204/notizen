package com.lk.backuprestore.listener

import com.lk.backuprestore.Result

/**
 * Erstellt von Lena am 2019-05-30.
 */
interface OnBackupFinished {

    fun onBackupFinished(result: Result)

}