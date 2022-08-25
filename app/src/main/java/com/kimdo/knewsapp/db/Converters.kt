package com.kimdo.knewsapp.db

import androidx.room.TypeConverter
import com.kimdo.knewsapp.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}