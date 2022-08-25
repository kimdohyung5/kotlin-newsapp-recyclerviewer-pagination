package com.kimdo.knewsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kimdo.knewsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){
    abstract val dao: ArticleDao
}

