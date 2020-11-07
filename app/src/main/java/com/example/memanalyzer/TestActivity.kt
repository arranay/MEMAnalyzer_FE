package com.example.memanalyzer

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.model.Img
import com.example.memanalyzer.model.Question
import com.example.memanalyzer.service.AllMemesApi
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_test.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList


class TestActivity : AppCompatActivity() {
    var numberOfQuestions: Int = 0
    var currentQuestion: Int = 0
    var questions: ArrayList<Question> = ArrayList()

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzerbackend.azurewebsites.net/api/Memes/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        progressBar.setVisibility(ProgressBar.VISIBLE)
        getAllMems()

        finish.setOnClickListener {
            if (questions.all { it.choice > 0 } && questions.size != 0) {
                val intent = Intent(this, TestResultActivity::class.java)
                val ids = questions.map { it.id }
                intent.putExtra("ids", ids.toLongArray())

                val choice = questions.map { it.choice }
                intent.putExtra("choice", choice.toIntArray())

                startActivity(intent)
                finish()
            } else {
                val toast = Toast.makeText(this, this.getString(R.string.not_answer_all), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show()
            }
        }

        back.setOnClickListener {
            currentQuestion--
            setQuestion()
        }

        forward.setOnClickListener {
            currentQuestion++
            setQuestion()
        }

        radioGroup.setOnCheckedChangeListener() { radioGroup: RadioGroup, i: Int ->
                if (radio_button_1.isChecked) questions[currentQuestion-1].choice = 5
                if (radio_button_2.isChecked) questions[currentQuestion-1].choice = 4
                if (radio_button_3.isChecked) questions[currentQuestion-1].choice = 3
                if (radio_button_4.isChecked) questions[currentQuestion-1].choice = 2
                if (radio_button_5.isChecked) questions[currentQuestion-1].choice = 1
        }
    }

    private fun getAllMems() {
        val memesApi: AllMemesApi = retrofit!!.create(AllMemesApi::class.java)
        val call: Call<List<Img>> = memesApi.getAllMemes()
        var data: List<Img>

        call.enqueue(object : Callback<List<Img>> {
            override fun onResponse(call: Call<List<Img>>, response: Response<List<Img>>) {
                data = response.body()!!
                for(i in data){
                    val question: Question = Question()
                    question.id = i.id
                    question.picture = i.picture
                    questions.add(question)
                }
                numberOfQuestions = questions.size
                timer()
                currentQuestion = 1
                setQuestion()
                progressBar.setVisibility(ProgressBar.INVISIBLE)
                content.setVisibility(ProgressBar.VISIBLE)
            }
            override fun onFailure(call: Call<List<Img>>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
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

    fun setQuestion() {
        isVisibleButton()
        numberOfQuestion.setText("$currentQuestion/$numberOfQuestions")
        setImg(questions[currentQuestion-1].picture)
        when (questions[currentQuestion-1].choice){
            5 -> radio_button_1.setChecked(true)
            4 -> radio_button_2.setChecked(true)
            3 -> radio_button_3.setChecked(true)
            2 -> radio_button_4.setChecked(true)
            1 -> radio_button_5.setChecked(true)
            else -> {
                radio_button_1.setChecked(true)
                questions[currentQuestion-1].choice = 5
            }
        }
    }

    fun setImg(url: String) {
        Picasso.with(this)
            .load(url)
            .resize(0, 500)
            .error(R.drawable.ic_baseline_error_outline_24)
            .into(mem)
    }

    fun isVisibleButton() {
        if (currentQuestion == 1) back.setVisibility(View.INVISIBLE)
        else back.setVisibility(View.VISIBLE)

        if (currentQuestion == numberOfQuestions) forward.setVisibility(View.INVISIBLE)
        else forward.setVisibility(View.VISIBLE)
    }
}