package com.example.memanalyzer

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memanalyzer.environment.RetrofitObject
import com.example.memanalyzer.model.User
import com.example.memanalyzer.service.UsersApi
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.fullName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences
    private val retrofit = RetrofitObject.retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        sign_up.setOnClickListener {
            checkEmptiness()
            checkPassword()
            checkEmail()
            checkUserName()
            checkFullName()

            if (fullName_input.error == null && userName_input.error == null && email_input.error == null
                && date_input.error == null && reg_password_input.error == null) {
                var user: User = User()
                user.userName = userName.text.toString()
                user.fullName = fullName.text.toString()
                user.email = email.text.toString()
                user.dateOfBirth = date.text.toString()
                user.password = reg_password.text.toString()
                user.gender = male.isChecked
                registration(user)
            }
        }

        Locale.setDefault(Locale.ENGLISH)
        date.showSoftInputOnFocus = false
        date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.setText("$dayOfMonth.$monthOfYear.$year")
            }, year, month, day)

            dpd.show()
        }
    }

    fun checkEmptiness() {
        userName_input.setError(if (userName.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        email_input.setError(if (email.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        fullName_input.setError(if (fullName.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        date_input.setError(if (date.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        reg_password_input.setError(if (reg_password.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        repeat_password_input.setError(if (repeat_password.text!!.trim().isEmpty()) resources.getString(R.string.emptyField) else null)
        gender_input.setError(if (male.isChecked || female.isChecked) null else resources.getString(R.string.gender_not_valid))
    }

    fun checkPassword() {
        if (!reg_password.text!!.isEmpty() && !repeat_password.text!!.isEmpty()) {
            if (reg_password.text!!.length < 8) reg_password_input.setError(resources.getString(R.string.passwords_length))

            if (reg_password.text.toString().indexOfAny(charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')) < 0)
                reg_password_input.setError(resources.getString(R.string.passwords_symbols))

            if (reg_password.text.toString().indexOfAny(charArrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')) < 0)
                reg_password_input.setError(resources.getString(R.string.passwords_symbols))

            if (reg_password.text.toString().indexOfAny(charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')) < 0)
                reg_password_input.setError(resources.getString(R.string.passwords_numbers))

            if (reg_password.text.toString().indexOfAny(charArrayOf('@', '!', '_')) < 0)
                reg_password_input.setError(resources.getString(R.string.passwords_sign))

            if (reg_password.text.toString() != repeat_password.text.toString())  {
                reg_password_input.setError(resources.getString(R.string.passwords_not_match))
                repeat_password_input.setError(" ")
            }
        }
    }

    fun checkEmail() {
        if (!email.text!!.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches())
            email_input.setError(resources.getString(R.string.email_not_valid))
    }

    fun checkUserName() {
        if (!userName.text!!.isEmpty() && userName.text!!.length < 5)
            userName_input.setError(resources.getString(R.string.user_name_not_valid))
    }

    fun checkFullName() {
        if (!fullName.text!!.isEmpty() && fullName.text!!.length < 10)
            fullName_input.setError(resources.getString(R.string.full_name_not_valid))
    }

    fun registration(user: User) {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<User> = usersApi.registration(user)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    var user = response.body()

                    var editor = sharedPreference.edit()
                    editor.putString("userName",user!!.userName)
                    editor.putString("fullName",user.fullName)
                    editor.putString("email",user.email)
                    editor.putString("token",user.token)
                    editor.putString("dateOfBirth",user.dateOfBirth)
                    editor.putBoolean("gender", user.gender)

                    editor.putString("statement", if (user.result !== null) user.result?.statement else "")
                    editor.putFloat("conservative", if (user.result !== null) user.result?.conservative!! else 0f)
                    editor.putFloat("domestic", if (user.result !== null) user.result?.domestic!! else 0f)
                    editor.putFloat("intellectual", if (user.result !== null) user.result?.intellectual!! else 0f)
                    editor.putFloat("pointless", if (user.result !== null) user.result?.pointless!! else 0f)
                    editor.putFloat("popular", if (user.result !== null) user.result?.popular!! else 0f)

                    editor.commit()

                    val intent = Intent(this@RegistrationActivity, AccountActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    when (response.code()) {
                        409 -> showToastr(resources.getString(R.string.data_not_unique))
                        else -> showToastr(resources.getString(R.string.registration_error))
                    }
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    fun showToastr(msg: String) {
        val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }
}