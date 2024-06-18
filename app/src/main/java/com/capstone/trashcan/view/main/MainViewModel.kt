package com.capstone.trashcan.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val client = OkHttpClient()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getTokenUser(): LiveData<String> {
        return repository.getSession().map { userModel ->
            userModel.token
        }.asLiveData()
    }

    val token = repository.getSession().map { it.token }.asLiveData()


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}