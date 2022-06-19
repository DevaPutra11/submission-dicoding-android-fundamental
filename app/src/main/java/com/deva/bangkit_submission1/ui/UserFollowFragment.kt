package com.deva.bangkit_submission1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deva.bangkit_submission1.R
import com.deva.bangkit_submission1.data.remote.response.UserListResponse
import com.deva.bangkit_submission1.data.remote.response.UserResponse
import com.deva.bangkit_submission1.data.remote.retrofit.ApiConfig
import com.deva.bangkit_submission1.databinding.FragmentUserFollowBinding
import com.deva.bangkit_submission1.ui.adapter.ListUserAdapter
import com.deva.bangkit_submission1.ui.detail.DetailUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserFollowFragment : Fragment() {

    private lateinit var rvFollow: RecyclerView
    private val list = ArrayList<UserResponse>()
    private lateinit var binding: FragmentUserFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollow = binding.rvFollow
        rvFollow.setHasFixedSize(true)

        if(activity!=null && isAdded){
            getListFollow()
        }

    }

    private fun getSingleUser(usernameee:String){
        val client = ApiConfig.getApiService().getDetailUser(usernameee)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (isAdded){
                    if (response.isSuccessful) {
                        showLoading(false)
                        val responseBody = response.body()
                        if (responseBody != null) {
                            showRvUser(responseBody)
                        }
                    } else {
                        Log.e(TAG, "getSingleUser Failed: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "getSingleUser onFailure: ${t.message}")
            }
        })
    }

    private fun showRvUser(users: UserResponse) {
        list.add(users)

        rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val listUserAdapter = ListUserAdapter(list)
        rvFollow.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                Intent(requireActivity(), DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                    startActivity(this)
                }
            }
        })
    }

    private fun getListFollow(){

        val usrname = arguments?.getString(ARG_USERNAME)

        when(arguments?.getInt(ARG_SECTION_NUMBER, 0)){
            1 -> {
                showLoading(true)
                val client = usrname?.let { ApiConfig.getApiService().getFollowers(it) }
                client?.enqueue(object : Callback<List<UserListResponse>> {
                    override fun onResponse(
                        call: Call<List<UserListResponse>>,
                        response: Response<List<UserListResponse>>
                    ) {
                        if (response.isSuccessful) {
                            if(isAdded){
                                showLoading(false)
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    val usr = setUsername(responseBody)
                                    if (usr.isEmpty()){
                                        Toast.makeText(requireContext(), getString(R.string.noFollowers), Toast.LENGTH_SHORT).show()
                                    } else{
                                        usr.forEach {
                                            getSingleUser(it)
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "List Follower Failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<List<UserListResponse>>, t: Throwable) {
                        showLoading(false)
                        Log.e(TAG, "List Follower onFailure: ${t.message}")
                    }
                })
            }
            2 -> {
                showLoading(true)
                val client = usrname?.let { ApiConfig.getApiService().getFollowing(it) }
                client?.enqueue(object : Callback<List<UserListResponse>> {
                    override fun onResponse(
                        call: Call<List<UserListResponse>>,
                        response: Response<List<UserListResponse>>
                    ) {
                        if (response.isSuccessful) {
                            if(isAdded){
                                showLoading(false)
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    val usr = setUsername(responseBody)
                                    if (usr.isEmpty()){
                                        Toast.makeText(requireContext(), getString(R.string.noFollowing), Toast.LENGTH_SHORT).show()
                                    } else{
                                        usr.forEach {
                                            getSingleUser(it)
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "List Following Failed: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<List<UserListResponse>>, t: Throwable) {
                        showLoading(false)
                        Log.e(TAG, "List Following onFailure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun setUsername(listUser:List<UserListResponse>):List<String>{
        val listUsername= ArrayList<String>()
        for(i in listUser){
            val usrnm = i.username
            listUsername.add(usrnm)
        }
        return listUsername
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "UserFollowFragment"
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "app_username"
    }
}