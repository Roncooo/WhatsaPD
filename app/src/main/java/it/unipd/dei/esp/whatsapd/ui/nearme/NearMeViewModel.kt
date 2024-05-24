package it.unipd.dei.esp.whatsapd.ui.nearme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * ViewModel for managing NearMe POIs.
 */
class NearMeViewModel(private val repository: PoiReviewRepository) : ViewModel() {
	
	private val allPois: Flow<List<Poi>> = repository.allPois
	fun getPoisByDistance(latitude: Double, longitude: Double): LiveData<List<PoiWrapper>> {
		
		val poiWrappers = allPois.map { poiList: List<Poi> ->
			poiList.map { poi: Poi ->
				PoiWrapper(poi, distance(latitude, longitude, poi.latitude, poi.longitude))
			}.sortedBy { it.distance }
		}
		return poiWrappers.asLiveData()
	}
	
	private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
		val phi1 = lat1 * Math.PI / 180
		val phi2 = lat2 * Math.PI / 180
		val deltaG = (lon1 - lon2) * Math.PI / 180
		val distanceInMeters = acos(
			sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(deltaG)
		) * EARTH_RADIUS_METERS
		return distanceInMeters / 1000
	}
	
	companion object {
		private const val EARTH_RADIUS_METERS = 6371e3
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