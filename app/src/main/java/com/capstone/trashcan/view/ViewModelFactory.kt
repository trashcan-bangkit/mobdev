package com.capstone.trashcan.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.data.WasteBankRepository
import com.capstone.trashcan.di.Injection
import com.capstone.trashcan.view.login.LoginViewModel
import com.capstone.trashcan.view.main.MainViewModel
import com.capstone.trashcan.view.profile.ProfileFragment
import com.capstone.trashcan.view.profile.ProfileViewModel
import com.capstone.trashcan.view.wastebank.WasteBankViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val wasteBankRepository: WasteBankRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(WasteBankViewModel::class.java) -> {
                WasteBankViewModel(wasteBankRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideWasteBankRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}