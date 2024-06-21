package com.capstone.trashcan.data

import androidx.lifecycle.LiveData
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity
import com.capstone.trashcan.data.retrofit.ApiService
import com.capstone.trashcan.data.room.ClassificationHistoryDao
import com.capstone.trashcan.utils.AppExecutors

class ClassificationHistoryRepository private constructor(
    private val apiService: ApiService,
    private val classificationHistoryDao : ClassificationHistoryDao,
    private val appExecutors: AppExecutors
){

    fun getAllHistoryClassification(): LiveData<List<ClassificationHistoryEntity>> {
        return classificationHistoryDao.getAllHistory();
    }

    fun insertClassificationHistory(classificationHistory: ClassificationHistoryEntity) {
        appExecutors.diskIO.execute {
            classificationHistoryDao.insertHistory(classificationHistory);
        }
    }

    companion object {
        @Volatile
        private var instance: ClassificationHistoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            classificationHistoryDao : ClassificationHistoryDao,
            appExecutors: AppExecutors
        ): ClassificationHistoryRepository =
            instance ?: synchronized(this) {
                instance ?: ClassificationHistoryRepository(apiService, classificationHistoryDao, appExecutors)
            }.also { instance = it }
    }
}