package com.capstone.trashcan.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.response.SignupResponse
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun registerUser(name: String, email: String, password: String, onResult: (SignupResponse?, String?) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val apiService = ApiConfig.getApiService()
                val response = apiService.register(name, email, password)
                _isLoading.value = false
                onResult(response, null)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, SignupResponse::class.java)
                onResult(null, errorResponse.message)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null, "An unexpected error occurred")
            }
        }
    }
}