package com.capstone.trashcan.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.trashcan.data.ClassificationHistoryRepository
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.di.Injection
import com.capstone.trashcan.view.history.HistoryViewModel
import com.capstone.trashcan.view.login.LoginViewModel
import com.capstone.trashcan.view.main.MainViewModel
import com.capstone.trashcan.view.profile.ProfileViewModel

class HistoryViewModelFactory(private val repository: ClassificationHistoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): HistoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = HistoryViewModelFactory(Injection.provideClassificationHistoryRepository(context))
                }
            }
            return INSTANCE as HistoryViewModelFactory
        }
    }
}