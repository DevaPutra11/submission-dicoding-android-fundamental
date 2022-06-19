package com.deva.bangkit_submission1.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("login")
	val username: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("public_repos")
	val publicRepos: Int
)

data class UserListResponse(
	@field:SerializedName("login")
	val username: String
)

data class SearchResponse(
	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("items")
	val items: ArrayList<ItemsItem>
)

data class ItemsItem(
	@field:SerializedName("login")
	val username: String,
)


