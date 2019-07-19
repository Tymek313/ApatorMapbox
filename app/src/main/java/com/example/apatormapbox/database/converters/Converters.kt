package com.example.apatormapbox.database.converters

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun doubleListToJson(list: List<Double?>): String {
        return list.joinToString(":")
    }

    @TypeConverter
    fun jsonToDoubleList(json: String): List<Double?> {
        return json.split(":".toRegex(), 0).map { it.toDouble() }
    }

    @TypeConverter
    fun jsonToIntList(json: String): List<Int?> {
        return json.split(":".toRegex(), 0).map { it.toInt() }
    }

    @TypeConverter
    fun intListToJson(list: List<Int?>): String {
        return list.joinToString(":")
    }

}