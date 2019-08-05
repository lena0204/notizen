package com.lk.backuprestore

/**
 * Erstellt von Lena am 2019-05-30.
 */
// TODO Test schreiben für String Parsing -> eigene Conversion und Datenstruktur (z.B. Stringarray
abstract class Conversion<T> {

    abstract fun toTableData(data: List<T>): TableData

    abstract fun fromTableData(data: TableData): List<T>

}