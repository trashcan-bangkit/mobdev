package com.capstone.trashcan.data.response

import com.google.gson.annotations.SerializedName

data class WastebankResponse(

	@field:SerializedName("nearest")
	val nearest: Nearest? = null,

	@field:SerializedName("others")
	val others: List<OthersItem?>? = null
)

data class Location(

	@field:SerializedName("lng")
	val lng: Any? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)

data class OthersItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: Location? = null
)

data class Nearest(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("distance")
	val distance: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: Location? = null
)
