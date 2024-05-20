package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Review
import kotlinx.coroutines.launch


class ReviewViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    fun getAllReviewsOfPoiByRating(searchString: String): LiveData<List<Review>> {
        return repository.getAllReviewsOfPoiByRating(searchString).asLiveData()
    }

    fun insert(review: Review) = viewModelScope.launch {
        repository.insert(review)
    }

}

class ReviewViewModelFactory(private val repository: PoiReviewRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
