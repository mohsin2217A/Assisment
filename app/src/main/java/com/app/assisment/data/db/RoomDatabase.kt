package com.app.assisment.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.assisment.common.Converters
import com.app.assisment.data.remote.model.UniversityResponseItem

@Database(entities = [UniversityResponseItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}