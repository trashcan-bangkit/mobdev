package com.capstone.trashcan.data

import com.capstone.trashcan.data.response.Nearest
import com.capstone.trashcan.data.response.OthersItem
import com.capstone.trashcan.data.response.WastebankResponse
import com.capstone.trashcan.data.retrofit.ApiService

class WasteBankRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun getNearestWasteBank(location: String): Nearest? {
        val response = apiService.getNearbyWasteBanks(location)
        return response.nearest?.let { nearest ->
            Nearest(
                address = nearest.address,
                name = nearest.name,
                location = nearest.location,
                distance = formatDistance(nearest.distance)
            )
        }
    }

    suspend fun getOtherWasteBanks(location: String): List<OthersItem?>? {
        val response = apiService.getNearbyWasteBanks(location)
        return response.others?.map { item ->
            OthersItem(
                address = item?.address,
                name = item?.name,
                location = item?.location,
                distance = formatDistance(item?.distance)
            )
        }
    }

    private fun formatDistance(distance: Any?): String? {
        return when (distance) {
            is Double -> {
                if (distance < 1000) {
                    String.format("%.2f m", distance)
                } else {
                    String.format("%.2f km", distance / 1000)
                }
            }
            is Int -> {
                if (distance < 1000) {
                    "$distance m"
                } else {
                    String.format("%.2f km", distance / 1000.0)
                }
            }
            is Long -> {
                if (distance < 1000) {
                    "$distance m"
                } else {
                    String.format("%.2f km", distance / 1000.0)
                }
            }
            else -> null
        }
    }

    companion object {
        @Volatile
        private var instance: WasteBankRepository? = null
        fun getInstance(apiService: ApiService): WasteBankRepository =
            instance ?: synchronized(this) {
                instance ?: WasteBankRepository(apiService)
            }.also { instance = it }
    }
}
