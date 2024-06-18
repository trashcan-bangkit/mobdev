package com.capstone.trashcan.di

import android.content.Context
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.data.pref.UserPreference
import com.capstone.trashcan.data.pref.dataStore
import com.capstone.trashcan.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}