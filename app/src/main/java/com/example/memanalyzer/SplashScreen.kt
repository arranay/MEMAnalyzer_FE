package com.example.memanalyzer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        Handler().postDelayed({
            intent = Intent(this, DescriptionActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}