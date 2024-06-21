package com.capstone.trashcan.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.UserRepository
import com.capstone.trashcan.data.pref.UserModel
import com.capstone.trashcan.data.response.LoginResponse
import com.capstone.trashcan.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginUser(email: String, password: String, onResult: (LoginResponse?, String?) -> Unit) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val apiService = ApiConfig.getApiService()
                val response = apiService.login(email, password)

                response.let { loginResult ->
                    val user = createSession(loginResult, email)
                    if (user != null) {
                        saveSession(user)
                    }
                }

                response.token?.let { ApiConfig.setToken(it) }

                _isLoading.value = false

                onResult(response, null)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                onResult(errorResponse, null)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null, "An unexpected error occurred")
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    private fun createSession(loginResponse: LoginResponse, email: String): UserModel? {
        return loginResponse.token?.let {
            UserModel(
                email = email,
                token = it,
                isLogin = true
            )
        }
    }
}