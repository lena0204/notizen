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