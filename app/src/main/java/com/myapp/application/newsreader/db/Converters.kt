package com.myapp.application.newsreader.db

import androidx.room.TypeConverter
import com.myapp.application.newsreader.models.Source
import okhttp3.internal.notify

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}