package com.capstone.trashcan.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity
import com.capstone.trashcan.data.entity.Converters


@Database(entities = [ClassificationHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClassificationHistoryDatabase : RoomDatabase() {
    abstract fun classificationHistoryDao(): ClassificationHistoryDao

    companion object {
        @Volatile
        private var instance: ClassificationHistoryDatabase? = null
        fun getInstance(context: Context): ClassificationHistoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ClassificationHistoryDatabase::class.java, "ClassificationHistory.db"
                ).build()
            }
    }
}