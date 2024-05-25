package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi

/**
 * [ViewModel] for managing a specific [Poi] (getting the [Poi] by the name and setting the favourite state).
 */
class PoiViewModel(private val repository: PoiReviewRepository) : ViewModel() {
	
	/**
	 * Retrieves the [Poi] with name [poiName].
	 */
	fun getPoiByName(poiName: String): LiveData<Poi> {
		return repository.getPoiByName(poiName).asLiveData()
	}
	
	/**
	 * Changes the favourite status of a [Poi].
	 */
	suspend fun setFavourite(poiName: String, newFavouriteValue: Boolean) {
		repository.updateFavourite(poiName, newFavouriteValue)
	}
	
}

/**
 * Factory class for creating an instance of [PoiViewModel].
 */
class PoiViewModelFactory(private val repository: PoiReviewRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(PoiViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST") return PoiViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
