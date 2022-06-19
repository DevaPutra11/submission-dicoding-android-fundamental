package com.deva.bangkit_submission1.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deva.bangkit_submission1.R
import com.deva.bangkit_submission1.data.remote.response.SearchResponse
import com.deva.bangkit_submission1.data.remote.response.UserListResponse
import com.deva.bangkit_submission1.data.remote.response.UserResponse
import com.deva.bangkit_submission1.data.remote.retrofit.ApiConfig
import com.deva.bangkit_submission1.databinding.ActivityMainBinding
import com.deva.bangkit_submission1.ui.FavoriteActivity
import com.deva.bangkit_submission1.ui.UserViewModel
import com.deva.bangkit_submission1.ui.UserViewModelFactory
import com.deva.bangkit_submission1.ui.detail.DetailUserActivity
import com.deva.bangkit_submission1.ui.adapter.ListUserAdapter
import com.deva.bangkit_submission1.ui.setting.SettingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<UserResponse>()
    private val tempList = ArrayList<SearchResponse>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var title:String

    private val listFav = ArrayList<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvUser
        rvUser.setHasFixedSize(true)

        list.clear()
        tempList.clear()

        search()
        getListUser()

        title = getString(R.string.title_main)
        setTitle(title)

        val userViewModel = obtainViewModel(this)
        userViewModel.getAllUser().observe(this,{userList->
            userList.forEach {
                listFav.add(it.isFavorited)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = UserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    private fun getSingleUser(usernameee:String?){
        val client = usernameee?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showRvUser(responseBody)
                    }
                } else {
                    Log.e(TAG, "Get Single User Failed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "Get Single User onFailure: ${t.message}")
            }
        })
    }

    private fun showRvUser(users: UserResponse) {
        list.add(users)

        rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                Intent(this@MainActivity, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                    startActivity(this)
                }
            }
        })
    }

    private fun getListUser(){
        showLoading(true)
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<List<UserListResponse>> {
            override fun onResponse(
                call: Call<List<UserListResponse>>,
                response: Response<List<UserListResponse>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    list.clear()
                    responseBody?.forEach {
                        getSingleUser(it.username)
                    }

                } else {
                    Log.e(TAG, "Get List User Failed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserListResponse>>, t: Throwable) {
                showLoading(false)
                Log.d(TAG, "onFailure: deva")
                Log.e(TAG, "Get List User onFailure: ${t.message}")
            }
        })
    }

    private fun searching(query:String?){
        showLoading(true)
        val client = query?.let { ApiConfig.getApiService().getSearch(it) }
        client?.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val size = responseBody?.totalCount
                    tempList.clear()

                    if (size == 0){
                        Toast.makeText(this@MainActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    } else{
                        responseBody?.let { tempList.add(it)}

                        responseBody?.items?.forEach {
                            getSingleUser(it.username)
                        }
                    }
                } else {
                    Log.e(TAG, "Searching Failed: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "Searching onFailure : ${t.message}")
            }
        })
    }


    private fun search(){
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.svSearch

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val search = query!!.lowercase(Locale.getDefault())
                if (search.isNotEmpty()){
                    searchView.clearFocus()
                    tempList.clear()
                    list.clear()
                    searching(search)
                } else{
                    tempList.clear()
                    list.clear()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val search = newText.lowercase(Locale.getDefault())
                if (search.isNotEmpty()){
                    list.clear()
                    tempList.clear()
                } else{
                    tempList.clear()
                    list.clear()
                    getListUser()
                }
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            R.id.menu2 -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setTitle(title:String){
        supportActionBar?.title = title
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}