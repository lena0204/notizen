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