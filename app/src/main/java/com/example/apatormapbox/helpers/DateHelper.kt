package com.example.apatormapbox.helpers

import android.icu.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateHelper {
    fun getTodayEQ(day: Long): String{
        val localDate = LocalDate.now().minusDays(day)
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        return date
    }

    fun getToday() = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
}