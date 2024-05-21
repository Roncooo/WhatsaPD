package it.unipd.dei.esp.whatsapd.ui.nearme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.ui.adapters.PoiWrapper

/**
 * ViewModel for managing NearMe POIs.
 */
class NearMeViewModel(private val repository: PoiReviewRepository) : ViewModel() {

    private val allPois: List<Poi> = repository.getPoisUnordered()
    fun getPoisByDistance(latitude: Double, longitude: Double): List<PoiWrapper> {

        var poiWrappers = allPois.map { poi ->
            PoiWrapper(poi, distance(latitude, longitude, poi.latitude, poi.longitude))
        }
        return poiWrappers.sortedBy { it.distance }
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val phi1 = lat1 * Math.PI / 180
        val phi2 = lat2 * Math.PI / 180
        val deltaG = (lon1 - lon2) * Math.PI / 180
        val dMeters = Math.acos(
            Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltaG)
        ) * EARTH_RADIUS
        val dKm = dMeters / 1000
        return dKm
    }

    companion object {
        private const val EARTH_RADIUS = 6371e3
    }

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