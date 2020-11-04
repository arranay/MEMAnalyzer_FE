package com.example.memanalyzer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_button.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
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