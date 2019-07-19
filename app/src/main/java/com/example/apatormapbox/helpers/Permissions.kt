package com.example.apatormapbox.helpers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object Permissions {

    fun handlePermission(fragment: Fragment, context: Context, permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            fragment.requestPermissions(arrayOf(permission), requestCode)
        }
    }
}