package com.deva.bangkit_submission1.data.remote.retrofit

import com.deva.bangkit_submission1.BuildConfig
import com.deva.bangkit_submission1.data.remote.response.SearchResponse
import com.deva.bangkit_submission1.data.remote.response.UserListResponse
import com.deva.bangkit_submission1.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getUser(): Call<List<UserListResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUser(
        @Path("username") username:String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserListResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserListResponse>>

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getSearch(
        @Query("q") query: String
    ): Call<SearchResponse>
}