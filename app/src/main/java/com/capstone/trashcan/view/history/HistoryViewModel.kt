package com.capstone.trashcan.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.capstone.trashcan.data.ClassificationHistoryRepository
import com.capstone.trashcan.data.entity.ClassificationHistoryEntity

class HistoryViewModel(private val classificationHistoryRepository : ClassificationHistoryRepository) : ViewModel() {
    fun getAllHistory() :LiveData<List<ClassificationHistoryEntity>>{
        return classificationHistoryRepository.getAllHistoryClassification()
    }

    fun insertHistory(classificationHistory: ClassificationHistoryEntity) {
        classificationHistoryRepository.insertClassificationHistory(classificationHistory)
    }

    fun getNonOrganicCount(): LiveData<Int> {
        return classificationHistoryRepository.getAllHistoryClassification().map { list ->
            list.count { it.mainCategory == "Anorganik" }
        }
    }

    fun getOrganicCount(): LiveData<Int> {
        return classificationHistoryRepository.getAllHistoryClassification().map { list ->
            list.count { it.mainCategory == "Organik" }
        }
    }

    fun getToxicCount(): LiveData<Int> {
        return classificationHistoryRepository.getAllHistoryClassification().map { list ->
            list.count { it.mainCategory == "B3" }
        }
    }
}