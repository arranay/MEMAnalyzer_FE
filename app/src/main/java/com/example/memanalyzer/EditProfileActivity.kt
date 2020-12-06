package com.example.memanalyzer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test_result.*

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}