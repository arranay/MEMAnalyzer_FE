package com.example.memanalyzer

import android.os.Bundle
import android.os.SystemClock
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
    }

    fun timer() {
        timer.isCountDown = true
        timer.base = SystemClock.elapsedRealtime() + numberOfQuestions * 30000
        timer.start()
        timer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - timer.base

            if (elapsedMillis >= 0) timer.stop()
            if (elapsedMillis/1000 == -30.toLong()) {
                val toast = Toast.makeText(this, "Thirty seconds left", Toast.LENGTH_SHORT)
                toast.show()
            }
            if (elapsedMillis/1000 == -60.toLong()) {
                val toast = Toast.makeText(this, "One minute left", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
