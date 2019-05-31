package com.lk.backuprestore

/**
 * Erstellt von Lena am 2019-05-30.
 */
abstract class Conversion<T> {

    // TODO to be adapted

    abstract fun toTableData(data: List<T>): TableData

    abstract fun fromTableData(data: TableData): List<T>

}