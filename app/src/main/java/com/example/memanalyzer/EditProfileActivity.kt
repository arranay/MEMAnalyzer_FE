package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.example.memanalyzer.environment.RetrofitObject
import com.example.memanalyzer.model.User
import com.example.memanalyzer.service.UsersApi
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_test_result.go_to_account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences
    private val retrofit = RetrofitObject.retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        setFields()

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        save_info.setOnClickListener {
            checkEmptiness()
            checkEmail()
            checkFullName()

            if (current_fullName_input.error == null && current_email_input.error == null && current_gender_input.error == null) {
                var user: User = User()
                user.fullName = current_fullName.text.toString()
                user.email = current_email.text.toString()
                user.gender = current_male.isChecked

                edirProfile(user)
            }
        }
    }

    fun edirProfile(user: User) {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<Any> = usersApi.editUser("Bearer " + sharedPreference.getString("token", ""), user)

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {

                    var editor = sharedPreference.edit()
                    editor.putString("fullName",user.fullName)
                    editor.putString("email",user.email)
                    editor.putBoolean("gender", user.gender)
                    editor.commit()

                    val intent = Intent(this@EditProfileActivity, AccountActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    when (response.code()) {
                        409 -> showToastr(resources.getString(R.string.data_not_unique))
                        else -> showToastr(resources.getString(R.string.registration_error))
                    }
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    fun setFields() {
        current_login.text = sharedPreference.getString("userName", "")
        current_fullName.setText(sharedPreference.getString("fullName", ""))
        current_email.setText(sharedPreference.getString("email", ""))

        if (sharedPreference.getBoolean("gender", true)) current_male.isChecked = true
        else current_female.isChecked = true
    }

    fun checkEmptiness() {
        current_email_input.setError(if (current_email.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        current_fullName_input.setError(if (current_fullName.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        current_gender_input.setError(if (current_male.isChecked || current_female.isChecked) null else resources.getString(R.string.gender_not_valid))
    }

    fun checkEmail() {
        if (!current_email.text!!.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(current_email.text).matches())
            current_email_input.setError(resources.getString(R.string.email_not_valid))
    }

    fun checkFullName() {
        if (!current_fullName.text!!.isEmpty() && current_fullName.text!!.length < 10)
            current_fullName_input.setError(resources.getString(R.string.full_name_not_valid))
    }

    fun showToastr(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }
}