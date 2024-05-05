package com.app.assisment.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun stringToListOfStrings(value: String): ArrayList<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun listOfStringsToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}