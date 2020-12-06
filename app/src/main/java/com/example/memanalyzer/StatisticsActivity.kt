package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.environment.RetrofitObject
import com.example.memanalyzer.model.Statistics
import com.example.memanalyzer.service.StatisticsApi
import com.jjoe64.graphview.ValueDependentColor
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.activity_users.go_to_account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StatisticsActivity : AppCompatActivity() {
    val TIME_RANGE = arrayOf<String?>(
        "for a day",
        "for a week",
        "for a month",
        "for half a year",
        "for a year",
        "for all time")
    var START = (1..100).toList()
    var END = (1..100).toList()
    var genderParam: Boolean? = null
    var startAgeParam: Int? = null
    var endAgeParam: Int? = null
    var lastDaysParam: Int? = null

    private val retrofit= RetrofitObject.retrofit

    lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        setAdapter()
        setValueChanged()
        getStatistics()

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        get_statistics.setOnClickListener {
            loadingStatistics.visibility = View.VISIBLE
            getStatistics()
        }
    }

    private fun getStatistics() {
        val usersApi: StatisticsApi = retrofit!!.create(StatisticsApi::class.java)
        val call: Call<Statistics> = usersApi.getStatistics("Bearer " + sharedPreference.getString("token", ""), genderParam, startAgeParam, endAgeParam, lastDaysParam)

        call.enqueue(object : Callback<Statistics> {
            override fun onResponse(call: Call<Statistics>, response: Response<Statistics>) {
                if (response.code() === 200){
                    var body = response.body()
                    buildGraf(body!!.domesticPersentage, body.popularPersentage, body.pointlessPersentage,
                        body.intellectualPersentage, body.conservativePersentage, body.negationPersentage, body.unpretentiousnessPersentage)
                } else {
                    showToastr(resources.getString(R.string.no_data_msg))
                    loadingStatistics.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<Statistics>, t: Throwable) {
                showToastr(resources.getString(R.string.statistics_msg))
                Log.v("retrofit", t.message!!)
            }
        })
    }

    private fun setAdapter() {
        time_range.showSoftInputOnFocus = false
        startAge.showSoftInputOnFocus = false
        endAge.showSoftInputOnFocus = false

        val adapter = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, TIME_RANGE)
        time_range.setAdapter(adapter)
        val adapterStart = ArrayAdapter<Number>(this, R.layout.dropdown_menu_popup_item, START)
        startAge.setAdapter(adapterStart)
        val adapterEnd = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, END)
        endAge.setAdapter(adapterEnd)

        startAge.setOnItemClickListener { parent, view, position, id ->
            startAgeParam = START[position]
            END = (START[position]..100).toList()
            val adapterEnd = ArrayAdapter<Any?>(this, R.layout.dropdown_menu_popup_item, END)
            endAge.setAdapter(adapterEnd)
        }

        endAge.setOnItemClickListener { parent, view, position, id ->
            endAgeParam = END[position]
            START = (1..END[position]).toList()
            val adapterStart = ArrayAdapter<Number>(this, R.layout.dropdown_menu_popup_item, START)
            startAge.setAdapter(adapterStart)
        }
    }

    fun  buildGraf(domesticPersentage: Double, popularPersentage: Double, pointlessPersentage: Double,
                   intellectualPersentage: Double, conservativePersentage: Double, negationPersentage: Double, unpretentiousnessPersentage: Double) {
        val series: BarGraphSeries<DataPoint> = BarGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(1.0, domesticPersentage),
                DataPoint(2.0, popularPersentage),
                DataPoint(3.0, pointlessPersentage),
                DataPoint(4.0, intellectualPersentage),
                DataPoint(5.0, conservativePersentage),
                DataPoint(6.0, negationPersentage),
                DataPoint(7.0, unpretentiousnessPersentage)
            )
        )

        val COLOR = mutableListOf<Int>()
        for (i in (0..6)) {
            COLOR.add(Color.rgb(
                (0..200).random(),
                (100..255).random(),
                (100..255).random()
            ))
        }

        series.valueDependentColor =
            ValueDependentColor { data ->
                COLOR[data.x.toInt()-1]
            }

        colorDom.setBackgroundColor(COLOR.get(0))
        colorPop.setBackgroundColor(COLOR.get(1))
        colorMeans.setBackgroundColor(COLOR.get(2))
        colorIntel.setBackgroundColor(COLOR.get(3))
        colorConserv.setBackgroundColor(COLOR.get(4))
        colorNeg.setBackgroundColor(COLOR.get(5))
        colorUnp.setBackgroundColor(COLOR.get(6))

        series.isAnimated = true
        graph.removeAllSeries()
        graph.addSeries(series)
        graph.getViewport().setMinY(0.0)

        loadingStatistics.visibility = View.INVISIBLE
    }

    private fun setValueChanged() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (all_gender.isChecked) genderParam = null
            if (male.isChecked) genderParam = true
            if (female.isChecked) genderParam = false
        }

        time_range.setOnItemClickListener { parent, view, position, id ->
            when (TIME_RANGE.get(position)) {
                "for a day" -> lastDaysParam = 1
                "for a week" -> lastDaysParam = 7
                "for a month" -> lastDaysParam = 31
                "for half a year" -> lastDaysParam = 183
                "for a year" -> lastDaysParam = 365
                "for all time" -> lastDaysParam = null
                else -> {
                    lastDaysParam = null
                }
            }
        }
    }

    fun showToastr(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }
}