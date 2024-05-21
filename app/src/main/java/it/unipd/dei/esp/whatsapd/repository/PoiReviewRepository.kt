package it.unipd.dei.esp.whatsapd.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.database.PoiDao
import it.unipd.dei.esp.whatsapd.repository.database.Review
import it.unipd.dei.esp.whatsapd.repository.database.ReviewDao
import kotlinx.coroutines.flow.Flow

/**
 * Defines methods to uniformly access data
 */
class PoiReviewRepository(private val poiDao: PoiDao, private val reviewDao: ReviewDao) {

    // get all pois
    fun getPoisUnordered(): List<Poi> = poiDao.getPoisUnordered()

    // get all pois alphabetized
    val allPoiAlphabetized: Flow<List<Poi>> = poiDao.getAllPoisAlphabetized()
    val favouritePoiAlphabetized: Flow<List<Poi>> = poiDao.getFavouritePoisAlphabetized()

    /**
     * Returns a Flow of Pois' List in alphabetized order
     */
    fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>> {
        return poiDao.getPoisByNameAlphabetized(searchedName)
    }

    /**
     * Returns a Flow of Pois' List in decreasing rating order
     */
    fun getAllReviewsOfPoiByRating(poi_name: String): Flow<List<Review>> {
        return reviewDao.getAllReviewsOfPoiByRating(poi_name)
    }

    /**
     * Returns Poi as LiveData whose name corresponds to searchedName
     */
    fun getPoiByName(searchedName: String): LiveData<Poi> {
        return poiDao.getPoiByName(searchedName)
    }

    /**
     * Can only be called inside a worker thread, makes Poi corresponding to poiName
     * a user's favourite Poi
     */
    @WorkerThread
    suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean) {
        poiDao.updateFavourite(poiName, newFavouriteValue)
    }

    /**
     * Can only be called inside a worker thread, inserts Poi in the database
     */
    @WorkerThread
    suspend fun insert(poi: Poi) {
        poiDao.insert(poi)
    }

    /**
     * Can only be called inside a worker thread, inserts Review in the database
     */
    @WorkerThread
    suspend fun insert(review: Review) {
        reviewDao.insert(review)
    }
}
