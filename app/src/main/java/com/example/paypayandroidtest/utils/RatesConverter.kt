package com.example.paypayandroidtest.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created RatesConverter to the handle the room database to save the data on the local DB with convert to map
 * */
object RatesConverter {

        @TypeConverter
        @JvmStatic
        fun fromString(value: String): Map<String, Double> {
            val mapType = object : TypeToken<Map<String, Double>>() {}.type
            return Gson().fromJson(value, mapType)
        }


        @TypeConverter
        @JvmStatic
        fun fromStringMap(map: Map<String, Double>): String {
            val gson = Gson()
            return gson.toJson(map)
        }
}