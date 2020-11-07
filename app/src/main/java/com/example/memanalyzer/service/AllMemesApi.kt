package com.example.memanalyzer.service

import com.example.memanalyzer.model.Img
import retrofit2.Call
import retrofit2.http.GET

interface AllMemesApi {

    @GET("AllMemes")
    fun getAllMemes(): Call<List<Img>>
}