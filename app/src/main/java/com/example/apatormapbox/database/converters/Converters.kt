package com.example.apatormapbox.database.converters

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun doubleListToJson(list: List<Double?>?): String? {
        if (list.isNullOrEmpty())
            return ""
        return list.joinToString(":")
    }

    @TypeConverter
    fun jsonToDoubleList(json: String?): List<Double?>? {
        if (json.isNullOrEmpty())
            return listOf()
        return json.split(":".toRegex(), 0).map { it.toDouble() }
    }

    @TypeConverter
    fun intListToJson(list: List<Int?>?): String? {
        if (list.isNullOrEmpty())
            return ""
        return list.joinToString(":")
    }

    @TypeConverter
    fun jsonToIntList(json: String?): List<Int?>? {
        if (json.isNullOrEmpty())
            return listOf()
        return json.split(":".toRegex(), 0).map { it.toInt() }
    }
}