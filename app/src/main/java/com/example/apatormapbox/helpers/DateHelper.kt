package com.example.apatormapbox.helpers

import android.icu.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun getToday() = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
}