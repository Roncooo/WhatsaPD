package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import kotlinx.coroutines.launch

class PoiViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    // LiveData holding a list of all POIs, ordered alphabetically.
    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

    fun getPoiByName(name: String): LiveData<Poi> {
        return repository.getPoiByName(name)
    }

    fun getAllPoisFilteredByName(searchString: String): LiveData<List<Poi>> {
        return repository.getPoisByNameAlphabetized(searchString).asLiveData()
    }

    /**
     * Changes the favourite status of a POI.
     */
    suspend fun changeFavourite(poiName: String, newFavouriteValue: Boolean) {
        repository.updateFavourite(poiName, newFavouriteValue)
    }

    fun insert(poi: Poi) = viewModelScope.launch {
        repository.insert(poi)
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
