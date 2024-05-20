package it.unipd.dei.esp.whatsapd.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository

class FavouritesViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    val favPois: LiveData<List<Poi>> = repository.favouritePoiAlphabetized.asLiveData()

}

class FavouritesViewModelFactory(private val repository: PoiReviewRepository) :

    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return FavouritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

