package it.unipd.dei.esp.whatsapd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PoiViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

    fun getPoiByName(name: String): LiveData<Poi> {
        return repository.getPoiByName(name)
    }

    fun getAllPoisFilteredByName(searchString: String): LiveData<List<Poi>> {
        return repository.getPoisByNameAlphabetized(searchString).asLiveData()
    }

    suspend fun changeFavourite(poiName: String, newFavouriteValue: Boolean) {
        /*
                var poiLiveData = getPoiByName(poiName)
                poiLiveData.observe()(object : Observer<Poi> {
                    var obs = this
                    override fun onChanged(poi: Poi) {
                        viewModelScope.launch {
                            repository.updateFavourite(poiName, !poi.favourite)
                            poiLiveData.removeObserver(obs)
                        }
                    }
                })
        */
        repository.updateFavourite(poiName, newFavouriteValue)
    }

    fun insert(poi: Poi) = viewModelScope.launch {
        repository.insert(poi)
    }

}


class PoiViewModelFactory(private val repository: PoiReviewRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PoiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
