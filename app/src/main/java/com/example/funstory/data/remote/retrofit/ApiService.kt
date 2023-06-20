package com.example.funstory.data.remote.retrofit

import com.example.funstory.data.remote.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegistrationResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("stories")
    @Multipart
    suspend fun addStory(
        @Part("description") description: String,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
        @Header("Authorization") authHeader: String,
    ): AddStory

    @GET("stories")
    suspend fun getListStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int? = 0,
        @Header("Authorization") authorization: String
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String
    ): DetailStory

}