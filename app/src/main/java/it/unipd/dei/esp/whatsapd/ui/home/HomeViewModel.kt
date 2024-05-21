package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository

/**
 * ViewModel for managing Home POIs.
 */
class HomeViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

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