package com.lk.notizen2.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

/**
 * Erstellt von Lena am 2019-05-31.
 */
class SharedPrefWrapper(private val context: Context) {

    fun readString(key: String): String {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getString(key, "") ?: ""
    }

    fun writeString(key: String, value: String) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit{
            putString(key, value)
        }
    }

    fun readInt(key: String, default: Int = 0): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getInt(key, default)
    }

    fun writeInt(key: String, value: Int) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit{
            putInt(key, value)
        }
    }

    fun readSet(key: String, default: Set<String>): Set<String> {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getStringSet(key, default)!!
    }

    fun writeSet(key: String, value: Set<String>) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit {
            putStringSet(key, value)
        }
    }

    fun readBoolean(key: String): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean(key, false)
    }

    fun writeBoolean(key: String, value: Boolean) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        sp.edit{
            putBoolean(key, value)
        }
    }
}