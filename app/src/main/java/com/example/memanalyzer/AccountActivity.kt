package com.example.memanalyzer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_account.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AccountActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        name.text = sharedPreference.getString("userName", "")
        fullName.text = sharedPreference.getString("fullName", "")

        val date = sharedPreference.getString("dateOfBirth", "")

        dateOfBirth.text = sharedPreference.getString("dateOfBirth", "")
    }
}