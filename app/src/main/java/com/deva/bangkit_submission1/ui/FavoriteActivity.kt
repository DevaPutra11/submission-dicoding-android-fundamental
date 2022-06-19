package com.deva.bangkit_submission1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.deva.bangkit_submission1.R
import com.deva.bangkit_submission1.databinding.ActivityFavoriteBinding
import com.deva.bangkit_submission1.ui.adapter.FavoriteAdapter

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding:ActivityFavoriteBinding?=null
    private val binding get() = _activityFavoriteBinding

    private lateinit var adapter: FavoriteAdapter

    private lateinit var title:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val userViewModel = obtainViewModel(this)
        userViewModel.getAllUser().observe(this,{userList->
            if (userList!=null){
                if(userList.size == 0){
                    Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show()
                }
                adapter.setListUser(userList)
            }
        })

        adapter = FavoriteAdapter()

        binding?.rvFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorite?.setHasFixedSize(true)
        binding?.rvFavorite?.adapter = adapter

        title = getString(R.string.title_favorite)
        setTitle(title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = UserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun setTitle(title:String){
        supportActionBar?.title = title
    }
}