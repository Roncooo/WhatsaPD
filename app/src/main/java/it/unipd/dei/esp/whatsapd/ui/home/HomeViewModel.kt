package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi

/**
 * ViewModel for managing Home Pois.
 */
class HomeViewModel(private val repository: PoiReviewRepository) : ViewModel() {
	
	val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()
	
	// In this case a function is necessary to pass the value of the searched name
	fun getPoisByNameAlphabetized(searchedName: String): LiveData<List<Poi>> {
		return repository.getPoisByNameAlphabetized(searchedName).asLiveData()
	}
	
}

/**
 * Factory class for creating an instance of HomeViewModel.
 */
class HomeViewModelFactory(private val repository: PoiReviewRepository) :
	
	ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST") return HomeViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}