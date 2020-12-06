package com.example.memanalyzer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memanalyzer.model.User
import com.example.memanalyzer.service.UsersApi
import kotlinx.android.synthetic.main.activity_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UsersActivity : AppCompatActivity() {

    lateinit var users: List<User>
    lateinit var sharedPreference: SharedPreferences
    var search = ""

    val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://memanalyzer.azurewebsites.net/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        getUsers()

        go_to_account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        button_search.setOnClickListener {
            search = textForSearch.text.toString()
            getUsers()
        }
    }

    fun getUsers() {
        val usersApi: UsersApi = retrofit!!.create(UsersApi::class.java)
        val call: Call<List<User>> = usersApi.getAllUsers("Bearer " + sharedPreference.getString("token", ""), search)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                users = response.body()!!

                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)

                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = CustomRecyclerAdapter(users, this@UsersActivity)

                progress.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.v("retrofit", t.message!!)
            }
        })
    }
}

class CustomRecyclerAdapter(val values: List<User>, val context: Context) : RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView?.text = values[position].userName
        holder.smallTextView?.text = values[position].email

        holder.go_to_user?.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            intent.putExtra("id", values[position].id)
            intent.putExtra("userName", values[position].userName)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var largeTextView: TextView? = null
        var smallTextView: TextView? = null
        var go_to_user: Button? = null

        init {
            largeTextView = itemView?.findViewById(R.id.textViewLarge)
            smallTextView = itemView?.findViewById(R.id.textViewSmall)
            go_to_user = itemView?.findViewById(R.id.go_to_user)
        }
    }
}
