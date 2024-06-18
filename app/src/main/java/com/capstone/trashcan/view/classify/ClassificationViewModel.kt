package com.capstone.trashcan.view.classify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.response.ClassificationResponse
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException

class ClassificationViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(
        multipartBody: MultipartBody.Part,
        onResult: (ClassificationResponse?, String?) -> Unit
    ){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val apiService = ApiConfig.getApiService()
                val successResponse = apiService.uploadImage(multipartBody)
                _isLoading.value = false

                onResult(successResponse, null)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ClassificationResponse::class.java)
                onResult(null, "error")
            }
        }
    }
}