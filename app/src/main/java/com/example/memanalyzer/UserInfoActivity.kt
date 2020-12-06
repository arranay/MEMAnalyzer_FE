package com.example.memanalyzer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.example.memanalyzer.model.User
import com.example.memanalyzer.service.UsersApi
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.activity_user_info.conservative
import kotlinx.android.synthetic.main.activity_user_info.description
import kotlinx.android.synthetic.main.activity_user_info.email
import kotlinx.android.synthetic.main.activity_user_info.everyday_life
import kotlinx.android.synthetic.main.activity_user_info.fullName
import kotlinx.android.synthetic.main.activity_user_info.intelligence
import kotlinx.android.synthetic.main.activity_user_info.meaninglessness
import kotlinx.android.synthetic.main.activity_user_info.name
import kotlinx.android.synthetic.main.activity_user_info.not_test
import kotlinx.android.synthetic.main.activity_user_info.popularity
import kotlinx.android.synthetic.main.activity_users.go_to_account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    var id: String = ""
    lateinit var sharedPreference: SharedPreferences

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzer.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val arguments = intent.extras
        id = arguments!!.get("id").toString()
        name.text = arguments!!.get("userName").toString()

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        setUserInfo()

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        block.setOnClickListener {
            blockUser()
        }
    }

    fun blockUser() {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<Any> = usersApi.block("Bearer " + sharedPreference.getString("token", ""), id)

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                block.visibility = View.INVISIBLE
                dateBlock.visibility = View.VISIBLE
                showToast()
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    fun showToast() {
        val toast = Toast.makeText(this, this.getString(R.string.successfully_blocked), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }

    fun setUserInfo() {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<User> = usersApi.getUserInfo("Bearer " + sharedPreference.getString("token", ""), id)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                var user = response.body()!!

                email.text = user.email
                fullName.text = user.fullName
                dateB.text = user.dateOfBirth
                gender.text = if (user.gender) resources.getString(R.string.male) else resources.getString(R.string.female)
                role.text = user.role.toString()

                if (user.result?.statement !== null) {
                    description.setText(user.result!!.statement)
                    conservative.setText(round(user.result!!.conservative))
                    intelligence.setText(round(user.result!!.intellectual))
                    popularity.setText(round(user.result!!.popular))
                    meaninglessness.setText(round(user.result!!.pointless))
                    everyday_life.setText(round(user.result!!.domestic))

                    test.visibility = View.VISIBLE
                } else {
                    not_test.text = resources.getString(R.string.user_not_test)
                    not_test.visibility = View.VISIBLE
                }

                if (user.isLocked!!) {
                    block.visibility = View.INVISIBLE
                    dateBlock.visibility = View.VISIBLE
                    dateBlock.text = user.lockoutEnd
                } else {
                    block.visibility = View.VISIBLE
                    dateBlock.visibility = View.INVISIBLE
                }


                user_info.visibility = View.VISIBLE
                progress.visibility = View.INVISIBLE
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }

    fun round(numb: Float): String {
        return String.format("%.1f", numb) + if (numb==100f) "%" else "%  "
    }
}