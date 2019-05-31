package com.lk.backuprestore.listener

import com.lk.backuprestore.TableData

/**
 * Erstellt von Lena am 2019-05-30.
 */
interface OnRestoreFinished {

    fun onRestoreFinished(data: TableData)

}