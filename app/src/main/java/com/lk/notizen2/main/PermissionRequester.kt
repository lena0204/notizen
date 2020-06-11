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

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Erstellt von Lena am 2019-07-18.
 */
@TargetApi(23)
class PermissionRequester (private val activityMain: MainActivity) {

    private val TAG = "PermissionRequester"

    companion object {
        const val PERMISSION_REQUEST_READ = 8009
        const val PERMISSION_REQUEST_WRITE = 8010
    }

    fun checkReadPermission(): Boolean{
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        return requestPermission(permissions, PERMISSION_REQUEST_READ)
    }

    fun checkWritePermission(): Boolean{
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return requestPermission(permissions, PERMISSION_REQUEST_WRITE)
    }

    private fun requestPermission(perm: Array<String>, requestCode: Int): Boolean{
        return if(hasToRequest(perm[0])){
            activityMain.requestPermissions(perm, requestCode)
            false
        } else {
            true
        }
    }

    private fun hasToRequest(permission: String): Boolean =
                ContextCompat.checkSelfPermission(activityMain, permission) != PackageManager.PERMISSION_GRANTED

}