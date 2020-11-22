package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.memanalyzer.model.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.conservative
import kotlinx.android.synthetic.main.activity_account.description
import kotlinx.android.synthetic.main.activity_account.everyday_life
import kotlinx.android.synthetic.main.activity_account.intelligence
import kotlinx.android.synthetic.main.activity_account.meaninglessness
import kotlinx.android.synthetic.main.activity_account.popularity
import kotlinx.android.synthetic.main.activity_test_result.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class AccountActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        if (sharedPreference.getString("role", "").equals(UserRole.Administrator.toString())) {
            users.visibility = Button.VISIBLE
            statistics.visibility = Button.VISIBLE
        }

        setUser()
        setTest()

        start_test.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
            finish()
        }

        exit.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.unlogin))
                .setNeutralButton(resources.getString(R.string.no), null)
                .setPositiveButton(resources.getString(R.string.yes))  { dialog, which ->
                    sharedPreference.edit().clear().apply()

                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .show()
        }

        users.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun setUser() {
        name.text = sharedPreference.getString("userName", "")
        fullName.text = sharedPreference.getString("fullName", "")
        email.text = sharedPreference.getString("email", "")
    }

    fun setTest() {
        if (sharedPreference.getString("statement", "") !== "") {
            description.text = sharedPreference.getString("statement", "")
            conservative.text = round(sharedPreference.getFloat("conservative", 0f))
            intelligence.text = round(sharedPreference.getFloat("intellectual", 0f))
            popularity.text = round(sharedPreference.getFloat("popular", 0f))
            meaninglessness.text = round(sharedPreference.getFloat("pointless", 0f))
            everyday_life.text = round(sharedPreference.getFloat("domestic", 0f))
            test.visibility = View.VISIBLE
        } else {
            not_test.text = resources.getString(R.string.not_test)
        }
    }

    fun round(numb: Float): String {
        return String.format("%.1f", numb) + if (numb==100f) "%" else "%  "
    }
}