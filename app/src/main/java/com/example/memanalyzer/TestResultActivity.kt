package com.example.memanalyzer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_test_result.*

class TestResultActivity : AppCompatActivity() {
    var button_status = Status.UP

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
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