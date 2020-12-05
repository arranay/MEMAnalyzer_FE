package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.memanalyzer.model.Statistics
import com.example.memanalyzer.service.StatisticsApi
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.activity_users.go_to_account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StatisticsActivity : AppCompatActivity() {
    val TIME_RANGE = arrayOf<String?>(
        "for a day",
        "for a week",
        "for a month",
        "for half a year",
        "for a year",
        "for the last five years")
    var START = (1..100).toList()
    var END = (1..100).toList()
    var genderParam = null
    var startAgeParam = null
    var endAgeParam = null
    var lastDaysParam = null

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzer.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        setAdapter()

        getStatistics()

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getStatistics() {
        val usersApi: StatisticsApi = retrofit!!.create(StatisticsApi::class.java)
        val call: Call<Statistics> = usersApi.getStatistics("Bearer " + sharedPreference.getString("token", ""), genderParam, startAgeParam, endAgeParam, lastDaysParam)

        call.enqueue(object : Callback<Statistics> {
            override fun onResponse(call: Call<Statistics>, response: Response<Statistics>) {
                var body = response.body()
            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    private fun setAdapter() {
        time_range.showSoftInputOnFocus = false
        startAge.showSoftInputOnFocus = false
        endAge.showSoftInputOnFocus = false

        val adapter = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, TIME_RANGE)
        time_range.setAdapter(adapter)
        val adapterStart = ArrayAdapter<Number>(this, R.layout.dropdown_menu_popup_item, START)
        startAge.setAdapter(adapterStart)
        val adapterEnd = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, END)
        endAge.setAdapter(adapterEnd)

        startAge.setOnItemClickListener { parent, view, position, id ->
            END = (START[position]..100).toList()
            val adapterEnd = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, END)
            endAge.setAdapter(adapterEnd)
        }

        endAge.setOnItemClickListener { parent, view, position, id ->
            START = (1..END[position]).toList()
            val adapterStart = ArrayAdapter<Number>(this, R.layout.dropdown_menu_popup_item, START)
            startAge.setAdapter(adapterStart)
        }
    }
}