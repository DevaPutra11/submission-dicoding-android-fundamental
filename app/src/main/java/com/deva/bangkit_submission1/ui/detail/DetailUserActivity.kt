package com.deva.bangkit_submission1.ui.detail


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.deva.bangkit_submission1.R
import com.deva.bangkit_submission1.data.local.entity.UserEntity
import com.deva.bangkit_submission1.data.remote.response.UserResponse
import com.deva.bangkit_submission1.data.remote.retrofit.ApiConfig
import com.deva.bangkit_submission1.databinding.ActivityDetailUserBinding
import com.deva.bangkit_submission1.ui.UserViewModelFactory
import com.deva.bangkit_submission1.ui.adapter.SectionsPagerAdapter
import com.deva.bangkit_submission1.ui.insert.UserInsertViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailUserBinding

    private lateinit var title:String
    private var usnm = ""

    private var user: UserEntity? = null

    private var _ischecked = false

    private lateinit var userInsertViewModel:UserInsertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        username?.let { getDetailUser(it) }

        usnm = username?.let { getUsername(it) }.toString()

        tabLayout()

        title = getString(R.string.title_detail)
        setTitle(title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fav = binding.toggleFavorite

        userInsertViewModel = obtainViewModel(this)

        fav.setOnClickListener {
            _ischecked = !_ischecked
            if(_ischecked){
                user?.let { it1 ->
                    userInsertViewModel.insert(it1)
                    userInsertViewModel.saveUser(it1)
                    showToast(getString(R.string.added))
                }
                if(user == null){
                    showToast(getString(R.string.addedFail))
                    _ischecked = false
                }
            } else{
                user?.let { it1 ->
                    userInsertViewModel.delete(it1)
                    userInsertViewModel.deleteUser(it1)
                    showToast(getString(R.string.deleted))
                }
                if(user == null){
                    showToast(getString(R.string.deletedFail))
                    _ischecked = true
                }
            }
            fav.isChecked = _ischecked
        }

        CoroutineScope(Dispatchers.IO).launch {
            val count:Int = userInsertViewModel.checkUser(username.toString())
            withContext(Dispatchers.Main){
                if (count>0){
                    fav.isChecked = true
                    _ischecked = true
                } else{
                    fav.isChecked = false
                    _ischecked = false
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserInsertViewModel {
        val factory = UserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserInsertViewModel::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getDetailUser(usernameee:String){
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(usernameee)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)

                        user = UserEntity(
                            responseBody.username,
                            responseBody.name,
                            responseBody.avatarUrl,
                            responseBody.followers,
                            responseBody.following,
                            responseBody.company,
                            responseBody.location,
                            responseBody.publicRepos,
                            false
                        )

                    }
                } else {
                    Log.e(TAG, "Gagal bos: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "Gagal: ${t.message}")
            }
        })
    }

    private fun setUserData(user: UserResponse){
        Glide.with(applicationContext)
            .load(user.avatarUrl)
            .into(binding.imgUser)

        with(binding){
            tvNameReceived.text = user.name
            tvUsernameReceived.text = user.username
            tvCompanyReceived.text = user.company
            tvLocationReceived.text = user.location
            tvRepositoryReceived.text = user.publicRepos.toString()
            tvFollowerReceived.text = user.followers.toString()
            tvFollowingReceived.text = user.following.toString()
            Glide.with(applicationContext)
                .load(user.avatarUrl)
                .into(binding.imgUser)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.with(applicationContext)
    }

    private fun tabLayout(){

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.appUsername = usnm
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setTitle(title:String){
        supportActionBar?.title = title
    }

    private fun getUsername(username:String):String{
        return username
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_FAVORITE = "extra_favorite"
        private const val TAG = "DetailUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}