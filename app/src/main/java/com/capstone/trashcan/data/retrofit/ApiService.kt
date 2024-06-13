package com.capstone.trashcan.data.retrofit

import com.capstone.trashcan.data.response.ClassificationResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ClassificationResponse
}