package com.intuit.catapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intuit.catapp.data.Breed
import com.intuit.catapp.data.CatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedsViewModel : ViewModel() {

    init {
        // Initialize the service for API calls
        CatService.init()
    }

    private val _breedsLiveData = MutableLiveData<BreedsResult>()
    val breedsLiveData: LiveData<BreedsResult>
        get() = _breedsLiveData

    fun getBreeds() {
        viewModelScope.launch(Dispatchers.IO) {
            val service = CatService.getService()

            val allBreeds = mutableListOf<Breed>()
            var page = 0

            try {
                while (true) {
                    val result = service.getBreeds(page, 10)
                    if (result.isEmpty()) break
                    allBreeds.addAll(result)
                    page++
                }
            } catch (e: Exception) {
                _breedsLiveData.postValue(BreedsResult(error = "Failed to fetch breeds: ${e.message}"))
                return@launch
            }

            if (allBreeds.isEmpty()) {
                _breedsLiveData.postValue(BreedsResult(error = "Empty list of breeds retrieved from API"))
            } else {
                _breedsLiveData.postValue(BreedsResult(data = allBreeds))
            }
        }
    }

    data class BreedsResult(val data: List<Breed>? = emptyList(), val error: String? = null)
}
