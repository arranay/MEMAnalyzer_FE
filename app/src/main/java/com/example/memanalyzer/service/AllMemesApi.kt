package com.example.memanalyzer.service

import com.example.memanalyzer.model.Answers
import com.example.memanalyzer.model.Img
import com.example.memanalyzer.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AllMemesApi {

    @GET("Memes/AllMemes")
    fun getAllMemes(): Call<List<Img>>

    @POST("Memes/Result")
    fun getResult(@Body answers: List<Answers>, @Header("Authorization") token: String): Call<Result>

}