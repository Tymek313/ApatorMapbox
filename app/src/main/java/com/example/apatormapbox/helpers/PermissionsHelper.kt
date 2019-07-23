package com.example.apatormapbox.helpers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionsHelper {

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun handlePermission(fragment: Fragment, context: Context, permission: String, requestCode: Int): Boolean {
        if (!isPermissionGranted(context, permission)) {
            fragment.requestPermissions(arrayOf(permission), requestCode)
            return false
        }
        return true
    }
}