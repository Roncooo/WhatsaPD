package it.unipd.dei.esp.whatsapd.ui.nearme

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [ViewModel] for managing ordered list of [PoiWrapper]s ([Poi] with distance from a given position).
 */
class NearMeViewModel(repository: PoiReviewRepository) : ViewModel() {
	
	private val allPois: Flow<List<Poi>> = repository.allPoi
	fun getPoisByDistance(otherLocation: Location): LiveData<List<PoiWrapper>> {
		
		val poiWrappers = allPois.map { poiList: List<Poi> ->
			poiList.map { poi: Poi ->
				val thisLocation = Location("")
				thisLocation.latitude = poi.latitude
				thisLocation.longitude = poi.longitude
				PoiWrapper(
					poi,
					otherLocation.distanceTo(thisLocation).toInt()
				)
			}.sortedBy { it.distance }
		}
		return poiWrappers.asLiveData()
	}
}

/**
 * Factory class for creating an instance of [NearMeViewModel].
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
