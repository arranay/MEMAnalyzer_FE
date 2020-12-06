package com.example.memanalyzer.environment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzer.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}