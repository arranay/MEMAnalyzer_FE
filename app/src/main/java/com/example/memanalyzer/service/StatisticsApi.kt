package com.example.memanalyzer.service

import com.example.memanalyzer.model.Statistics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StatisticsApi {

    @GET("Statistics")
    fun getStatistics(
        @Header("Authorization") token: String,
        @Query("gender") gender: Boolean?,
        @Query("startAge") startAge: Int?,
        @Query("endAge") endAge: Int?,
        @Query("lastDays") lastDays: Int?
    ): Call<Statistics>
}