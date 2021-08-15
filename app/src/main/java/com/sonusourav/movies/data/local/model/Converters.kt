package com.sonusourav.movies.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()
    @JvmStatic
    @TypeConverter
    fun fromGenresList(genres: List<Genre>): String {
        return gson.toJson(genres)
    }

    @JvmStatic
    @TypeConverter
    fun toGenresList(genres: String?): List<Genre> {
        if (genres == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genres, listType)
    }
}