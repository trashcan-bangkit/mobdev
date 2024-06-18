package com.capstone.trashcan.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.data.response.ClassificationResponse
import com.capstone.trashcan.data.response.UserProfileResponse
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException

class ProfileViewModel(private val repository: UserRepository)  : ViewModel() {
    private val _profile = MutableLiveData<UserProfileResponse>()
    val profile: LiveData<UserProfileResponse> get() = _profile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiServiceWithToken()
                val successResponse = apiService.getUserProfile()
                _profile.value = successResponse
                _isLoading.value = false
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ClassificationResponse::class.java)
                _error.value = "error"
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}