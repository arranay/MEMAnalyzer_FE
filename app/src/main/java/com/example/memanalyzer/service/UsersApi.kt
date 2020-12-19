package com.example.memanalyzer.service

import com.example.memanalyzer.model.Block
import com.example.memanalyzer.model.User
import com.example.memanalyzer.model.UserLogin
import retrofit2.Call
import retrofit2.http.*

interface UsersApi {
    @POST("Users/Login")
    fun login(@Body user: UserLogin): Call<User>

    @POST("Users/Register")
    fun registration(@Body user: User): Call<User>

    @GET("Users")
    fun getAllUsers(@Header("Authorization") token: String?, @Query("search") search: String?): Call<List<User>>

    @GET("Users/{userId}")
    fun getUserInfo(@Header("Authorization") token: String?, @Path("userId") userId: String): Call<User>

    @PUT("Users/Block/{userId}")
    fun block(@Header("Authorization") token: String?, @Path("userId") userId: String): Call<Block>

    @PUT("Users/Profile")
    fun editUser(@Header("Authorization") token: String?, @Body user: User): Call<Any>
}