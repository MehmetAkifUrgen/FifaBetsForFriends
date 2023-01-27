package com.example.fifabet.db

import androidx.room.TypeConverter
import com.google.gson.Gson


class Converters2 {

    @TypeConverter
    fun listToJson(value: List<Bahis>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Bahis>::class.java).toList()
}