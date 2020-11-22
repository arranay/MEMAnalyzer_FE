package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.model.Answers
import com.example.memanalyzer.model.Result
import com.example.memanalyzer.service.AllMemesApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_test_result.*
import kotlinx.android.synthetic.main.activity_test_result.exit_button
import kotlinx.android.synthetic.main.activity_test_result.go_to_account
import kotlinx.android.synthetic.main.activity_test_result.logIn
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

    lateinit var sharedPreference: SharedPreferences
    var token: String = ""

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzerbackend.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference.getString("token", "") !== "") {
            go_to_account.visibility = Button.VISIBLE
            logIn.visibility = Button.INVISIBLE
            token = sharedPreference.getString("token", "").toString()

            go_to_account.setOnClickListener {
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

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

        logIn.setOnClickListener {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
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
        val call: Call<Result> = memesApi.getResult(answers, token)

        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val testResult = response.body()
                description.setText(testResult!!.statement)
                conservative.setText(round(testResult.conservative))
                intelligence.setText(round(testResult.intellectual))
                popularity.setText(round(testResult.popular))
                meaninglessness.setText(round(testResult.pointless))
                everyday_life.setText(round(testResult.domestic))

                if (token != "") {
                    var editor = sharedPreference.edit()
                    editor.putString("statement", testResult!!.statement)
                    editor.putFloat("conservative", testResult.conservative)
                    editor.putFloat("domestic", testResult.domestic)
                    editor.putFloat("intellectual", testResult.intellectual)
                    editor.putFloat("pointless", testResult.pointless)
                    editor.putFloat("popular", testResult.popular)
                    editor.commit()
                }

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