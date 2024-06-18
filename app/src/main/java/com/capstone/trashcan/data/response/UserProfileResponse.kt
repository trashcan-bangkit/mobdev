package com.capstone.trashcan.data.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class ProfileUser(

	@field:SerializedName("email")
	val email: String? = null
)
