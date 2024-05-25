package it.unipd.dei.esp.whatsapd.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi

/**
 * [ViewModel] for managing favourite [Poi]s.
 */
class FavouritesViewModel(repository: PoiReviewRepository) : ViewModel() {
	
	val favPoi: LiveData<List<Poi>> = repository.favouritePoiAlphabetized.asLiveData()
	
}

/**
 * Factory class for creating an instance of [FavouritesViewModel].
 */
class FavouritesViewModelFactory(private val repository: PoiReviewRepository) :
	
	ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST") return FavouritesViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
