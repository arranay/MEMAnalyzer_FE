package com.example.memanalyzer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.memanalyzer.model.User
import com.example.memanalyzer.model.UserLogin
import com.example.memanalyzer.service.UsersApi
import kotlinx.android.synthetic.main.activity_authorization.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.content.SharedPreferences
import android.view.Gravity
import android.widget.Toast

@Suppress("SENSELESS_COMPARISON")
class AuthorizationActivity : AppCompatActivity() {

    lateinit var sharedPreference: SharedPreferences

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzerbackend.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        sign_in.setOnClickListener {

            if (password.text!!.isEmpty()) password_input.setError(resources.getString(R.string.emptyField))
            if (userName.text!!.isEmpty()) userName_input.setError(resources.getString(R.string.emptyField))

            if (!password.text!!.isEmpty() && !userName.text!!.isEmpty()) {
                password_input.setError(null)
                userName_input.setError(null)

                var userLogin: UserLogin = UserLogin()
                userLogin.userName = userName.text.toString()
                userLogin.password = password.text.toString()
                login(userLogin)
            }
        }

        sign_up.setOnClickListener {
            val intent = Intent(this@AuthorizationActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(userLogin: UserLogin) {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<User> = usersApi.login(userLogin)

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
                    editor.putString("role", user.role.toString())

                    editor.putString("statement", if (user.result !== null) user.result?.statement else "")
                    editor.putFloat("conservative", if (user.result !== null) user.result?.conservative!! else 0f)
                    editor.putFloat("domestic", if (user.result !== null) user.result?.domestic!! else 0f)
                    editor.putFloat("intellectual", if (user.result !== null) user.result?.intellectual!! else 0f)
                    editor.putFloat("pointless", if (user.result !== null) user.result?.pointless!! else 0f)
                    editor.putFloat("popular", if (user.result !== null) user.result?.popular!! else 0f)

                    editor.commit()

                    val intent = Intent(this@AuthorizationActivity, AccountActivity::class.java)
                    startActivity(intent)
                    finish()

                    showToastr("Welcome to MEMAnalyzer, " + user.userName)
                } else {
                    when (response.code()) {
                        401 -> showToastr(resources.getString(R.string.unauthorized))
                        409 -> showToastr(resources.getString(R.string.locked))
                        else -> showToastr(resources.getString(R.string.authorization_error))
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