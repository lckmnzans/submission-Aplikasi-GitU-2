package com.dicoding.gitu.response

import com.google.gson.annotations.SerializedName

data class UserFollowResponse(

	@field:SerializedName("UserFollowResponse")
	val userFollowResponse: List<UserFollowResponseItem>
)

data class UserFollowResponseItem(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("node_id")
	val nodeId: String
)
