package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository

/**
 * ViewModel for managing NearMe POIs.
 */
class NearMeViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    // todo should calculate distances and order the pois
    // LiveData holding a list of all POIs, ordered alphabetically.
    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

}

/**
 * Factory class for creating an instance of NearMeViewModel.
 */
class NearMeViewModelFactory(private val repository: PoiReviewRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearMeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NearMeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}