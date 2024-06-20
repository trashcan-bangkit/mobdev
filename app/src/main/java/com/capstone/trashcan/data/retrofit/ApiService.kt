package com.capstone.trashcan.data.retrofit

import com.capstone.trashcan.data.response.ClassificationResponse
import com.capstone.trashcan.data.response.LoginResponse
import com.capstone.trashcan.data.response.Response
import com.capstone.trashcan.data.response.SignupResponse
import com.capstone.trashcan.data.response.UserProfileResponse
import com.capstone.trashcan.data.response.WastebankResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("recommend")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): ClassificationResponse

    @FormUrlEncoded
    @POST("signup")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignupResponse

    @FormUrlEncoded
    @POST("signin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("profile")
    suspend fun getUserProfile(): UserProfileResponse

    @GET("banksampah-terdekat")
    suspend fun getNearbyWasteBanks(
        @Query("location") location: String
    ): WastebankResponse
}