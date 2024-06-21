package com.capstone.trashcan.view.wastebank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashcan.data.WasteBankRepository
import com.capstone.trashcan.data.response.Nearest
import com.capstone.trashcan.data.response.OthersItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WasteBankViewModel(
    private val wasteBankRepository: WasteBankRepository
) : ViewModel() {

    private val _nearestWasteBank = MutableLiveData<Nearest?>()
    val nearestWasteBank: LiveData<Nearest?> get() = _nearestWasteBank

    private val _otherWasteBanks = MutableLiveData<List<OthersItem?>?>()
    val otherWasteBanks: LiveData<List<OthersItem?>?> get() = _otherWasteBanks

    private val _filteredWasteBanks = MutableLiveData<List<OthersItem?>>()
    val filteredWasteBanks: LiveData<List<OthersItem?>> get() = _filteredWasteBanks

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun fetchWasteBanks(location: String) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val nearest = wasteBankRepository.getNearestWasteBank(location)
                val others = wasteBankRepository.getOtherWasteBanks(location)
                _nearestWasteBank.postValue(nearest)
                _otherWasteBanks.postValue(others)
                _loadingState.value = false
            } catch (e: HttpException) {
                _errorMessage.value = "An error occurred while fetching waste banks."
            } catch (e: IOException) {
                _errorMessage.value = "Network error. Please check your internet connection."
            }
        }
    }

    fun fetchNearbyWasteBanks(location: String) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val nearestWasteBank = wasteBankRepository.getNearestWasteBank(location)
                val otherWasteBanks = wasteBankRepository.getOtherWasteBanks(location)

                val filteredWasteBanks = mutableListOf<OthersItem?>()
                nearestWasteBank?.let {
                    filteredWasteBanks.add(
                        OthersItem(
                            address = it.address,
                            name = it.name,
                            location = it.location,
                            distance = it.distance
                        )
                    )
                }
                otherWasteBanks?.let {
                    filteredWasteBanks.addAll(it.take(2))
                }

                _filteredWasteBanks.postValue(filteredWasteBanks)
                _loadingState.value = false
            } catch (e: HttpException) {
                _loadingState.value = false
                _errorMessage.value = "An error occurred while fetching waste banks."
            } catch (e: IOException) {
                _loadingState.value = false
                _errorMessage.value = "Network error. Please check your internet connection."
            }
        }
    }
}
