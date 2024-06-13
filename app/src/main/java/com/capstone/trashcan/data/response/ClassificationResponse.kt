package com.capstone.trashcan.data.response

import com.google.gson.annotations.SerializedName

data class ClassificationResponse(

	@field:SerializedName("sub_category")
	val subCategory: String? = null,

	@field:SerializedName("main_category")
	val mainCategory: String? = null
)
