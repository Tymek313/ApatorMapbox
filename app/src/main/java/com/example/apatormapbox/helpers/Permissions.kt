package com.example.apatormapbox.helpers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Permissions {

    fun handlePermission(activity: Activity, permission: String, requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext!!,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }
}