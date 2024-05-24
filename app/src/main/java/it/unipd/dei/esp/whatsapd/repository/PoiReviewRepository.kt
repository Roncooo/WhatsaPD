package it.unipd.dei.esp.whatsapd.repository

import androidx.annotation.WorkerThread
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.database.PoiDao
import it.unipd.dei.esp.whatsapd.repository.database.Review
import it.unipd.dei.esp.whatsapd.repository.database.ReviewDao
import kotlinx.coroutines.flow.Flow

/**
 * Defines methods to uniformly access data
 */
class PoiReviewRepository(private val poiDao: PoiDao, private val reviewDao: ReviewDao) {
	
	val allPois: Flow<List<Poi>> = poiDao.getAllPois()
	val allPoiAlphabetized: Flow<List<Poi>> = poiDao.getAllPoisAlphabetized()
	val favouritePoiAlphabetized: Flow<List<Poi>> = poiDao.getFavouritePoisAlphabetized()
	
	/**
	 * Returns, in alphabetized order, [Poi]s that contain [searchedName] in their name.
	 */
	fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>> {
		return poiDao.getPoisByNameAlphabetized(searchedName)
	}
	
	/**
	 * Returns [Review]s of [Poi] with id [poi_name] in decreasing rating order.
	 * [poi_name] must match an existing [Poi].
	 */
	fun getAllReviewsOfPoiByRating(poi_name: String): Flow<List<Review>> {
		return reviewDao.getAllReviewsOfPoiByRating(poi_name)
	}
	
	/**
	 * Returns the [Poi] (if it exists) whose name corresponds to [searchedName]
	 */
	fun getPoiByName(searchedName: String): Flow<Poi> {
		return poiDao.getPoiByName(searchedName)
	}
	
	/**
	 * Can only be called inside a worker thread. Sets the value of favourite attribute for the
	 * [Poi] with name [poiName]. If the name is wrong nothing happens.
	 */
	@WorkerThread
	suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean) {
		poiDao.updateFavourite(poiName, newFavouriteValue)
	}
	
	/**
	 * Can only be called inside a worker thread, inserts [poi] in the database.
	 */
	@WorkerThread
	suspend fun insert(poi: Poi) {
		poiDao.insert(poi)
	}
	
	/**
	 * Can only be called inside a worker thread, inserts [review] in the database.
	 */
	@WorkerThread
	suspend fun insert(review: Review) {
		reviewDao.insert(review)
	}
}
