package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NearMeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is nearme Fragment"
    }
    val text: LiveData<String> = _text
}