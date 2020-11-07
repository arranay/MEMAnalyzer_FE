package com.example.memanalyzer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.model.Question
import kotlinx.android.synthetic.main.activity_test_result.*

class TestResultActivity : AppCompatActivity() {
    var button_status = Status.UP
    lateinit var ids: LongArray
    lateinit var choice: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)

        val arguments = intent.extras
        ids = arguments!!.get("ids") as LongArray
        choice = arguments!!.get("choice") as IntArray

        ShowAllCategoryButton()
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
}

enum class Status {
    UP, DOWN
}