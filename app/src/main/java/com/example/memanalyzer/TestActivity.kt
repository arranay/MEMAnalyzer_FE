package com.example.memanalyzer

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    var numberOfQuestions: Int = 3
    var currentQuestion: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        timer()

        finish.setOnClickListener {
            val intent = Intent(this, TestResultActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun timer() {
        timer.isCountDown = true
        timer.base = SystemClock.elapsedRealtime() + numberOfQuestions * 30000
        timer.start()
        timer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - timer.base

            if (elapsedMillis >= 0) timer.stop()
            if (elapsedMillis/1000 == -30.toLong()) {
                val toast = Toast.makeText(this, this.getString(R.string.thirty_second), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show()
            }
            if (elapsedMillis/1000 == -60.toLong()) {
                val toast = Toast.makeText(this, this.getString(R.string.one_minute), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show()
            }
        }
    }
}
