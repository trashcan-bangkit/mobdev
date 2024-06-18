package com.capstone.trashcan.data.response

import com.google.gson.annotations.SerializedName

data class ClassificationResponse(

	@field:SerializedName("sub_category")
	val subCategory: String? = null,

	@field:SerializedName("main_category")
	val mainCategory: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<String?>? = null

)
