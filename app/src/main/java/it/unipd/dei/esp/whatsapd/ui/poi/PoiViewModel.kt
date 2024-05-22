package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi

class PoiViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    fun getPoiByName(name: String): LiveData<Poi> {
        return repository.getPoiByName(name).asLiveData()
    }

    /**
     * Changes the favourite status of a Poi.
     */
    suspend fun setFavourite(poiName: String, newFavouriteValue: Boolean) {
        repository.updateFavourite(poiName, newFavouriteValue)
    }

}

/**
 * Factory class for creating an instance of PoiViewModel.
 */
class PoiViewModelFactory(private val repository: PoiReviewRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PoiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
