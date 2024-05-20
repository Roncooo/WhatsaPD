package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository

class NearMeViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    // todo should calculate distances and order the pois
    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

}


class NearMeViewModelFactory(private val repository: PoiReviewRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearMeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NearMeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}