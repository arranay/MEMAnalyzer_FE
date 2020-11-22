package com.example.memanalyzer.service

import com.example.memanalyzer.model.User
import com.example.memanalyzer.model.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("Users/Login")
    fun login(@Body user: UserLogin): Call<User>

    @POST("Users/Register")
    fun registration(@Body user: User): Call<User>
}