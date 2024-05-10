package it.unipd.dei.esp.whatsapd.ui.poi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PoiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        // value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}