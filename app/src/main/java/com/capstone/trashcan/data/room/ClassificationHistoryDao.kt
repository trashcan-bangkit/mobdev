package com.capstone.trashcan.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity

@Dao
interface ClassificationHistoryDao {
    @Query("SELECT * FROM classification_history")
    fun getAllHistory(): LiveData<List<ClassificationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(classificationHistory: ClassificationHistoryEntity)
}