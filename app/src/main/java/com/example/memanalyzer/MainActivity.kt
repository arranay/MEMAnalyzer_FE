package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.exit_button
import kotlinx.android.synthetic.main.activity_main.go_to_account
import kotlinx.android.synthetic.main.activity_main.logIn
import kotlinx.android.synthetic.main.activity_test_result.*

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var sh = sharedPreference.getString("token", "")
        if (sharedPreference.getString("token", "") !== "") {
            go_to_account.visibility = Button.VISIBLE
            logIn.visibility = Button.INVISIBLE

            go_to_account.setOnClickListener {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        start_button.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }

        logIn.setOnClickListener {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }

        exit_button.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.exit_msg))
                .setNeutralButton(resources.getString(R.string.no), null)
                .setPositiveButton(resources.getString(R.string.yes))  { dialog, which ->
                    finish()
                }
                .show()
        }
    }
}