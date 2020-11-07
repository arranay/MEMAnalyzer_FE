package com.example.memanalyzer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.model.Answers
import com.example.memanalyzer.model.Result
import com.example.memanalyzer.service.AllMemesApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_test_result.*
import kotlinx.android.synthetic.main.activity_test_result.exit_button
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestResultActivity : AppCompatActivity() {
    var button_status = Status.UP
    lateinit var ids: LongArray
    lateinit var choice: IntArray
    var answers: ArrayList<Answers> = ArrayList()

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzerbackend.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)

        val arguments = intent.extras
        ids = arguments!!.get("ids") as LongArray
        choice = arguments!!.get("choice") as IntArray
        ids.forEach { answers.add(Answers(it, choice[ids.indexOf(it)])) }

        getResult()
        ShowAllCategoryButton()

        restart_button.setOnClickListener {
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

    fun ShowAllCategoryButton() {
        all_category_button.setOnClickListener {
            if (button_status === Status.UP) {
                all_category.text = this.getString(R.string.all_categories)
                all_category_button.setIconResource(R.drawable.ic_baseline_arrow_upward_24)
                button_status = Status.DOWN
            } else {
                all_category.text = ""
                all_category_button.setIconResource(R.drawable.ic_sharp_arrow_downward_24)
                button_status = Status.UP
            }
        }
    }

    fun getResult() {
        val memesApi: AllMemesApi = retrofit!!.create(AllMemesApi::class.java)
        val call: Call<Result> = memesApi.getResult(answers)

        call.enqueue(object : Callback<Result> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val testResult = response.body()
                description.setText(testResult!!.statement)
                conservative.setText(round(testResult.conservative))
                intelligence.setText(round(testResult.intellectual))
                popularity.setText(round(testResult.popular))
                meaninglessness.setText(round(testResult.pointless))
                everyday_life.setText(round(testResult.domestic))

                result.setVisibility(ProgressBar.VISIBLE)
                progress.setVisibility(ProgressBar.INVISIBLE)
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    fun round(numb: Float): String {
        return String.format("%.1f", numb) + if (numb==100f) "%" else "%  "
    }
}

enum class Status {
    UP, DOWN
}