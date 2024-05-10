package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// this should take as a parameter private val repository: PoiReviewRepository (or shouldn't?)
class PoiViewModel : ViewModel() {

    // todo remove text and _text
    private val _text = MutableLiveData<String>().apply {
        // value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // not sure about this, maybe this should return just one poi
    /*
    val allPois: LiveData<List<Poi>> = repository.allPoiAlphabetized.asLiveData()

    fun insert(poi: Poi) = viewModelScope.launch {
        repository.insert(poi)
    }

     */
}
