package it.unipd.dei.esp.whatsapd.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import it.unipd.dei.esp.whatsapd.repository.database.Poi
import it.unipd.dei.esp.whatsapd.repository.database.PoiDao
import it.unipd.dei.esp.whatsapd.repository.database.Review
import it.unipd.dei.esp.whatsapd.repository.database.ReviewDao
import kotlinx.coroutines.flow.Flow

class PoiReviewRepository(private val poiDao: PoiDao, private val reviewDao: ReviewDao) {

    // get all pois
    val allPoi: Flow<List<Poi>> = poiDao.getAllPois()

    // get all pois alphabetized
    val allPoiAlphabetized: Flow<List<Poi>> = poiDao.getAllPoisAlphabetized()

    // get favourite pois alphabetized
    val favouritePoiAlphabetized: Flow<List<Poi>> = poiDao.getFavouritePoisAlphabetized()

    // define a getter function with a param
    fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>> {
        return poiDao.getPoisByNameAlphabetized(searchedName)
    }

    // define a getter function with a param
    fun getAllReviewsOfPoiByRating(poi_name: String): Flow<List<Review>> {
        return reviewDao.getAllReviewsOfPoiByRating(poi_name)
    }

    fun getPoiByName(searchedName: String): LiveData<Poi> {
        return poiDao.getPoiByName(searchedName)
    }

    @WorkerThread
    suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean) {
        poiDao.updateFavourite(poiName, newFavouriteValue)
    }

    @WorkerThread
    suspend fun insert(poi: Poi) {
        poiDao.insert(poi)
    }

    @WorkerThread
    suspend fun insert(review: Review) {
        reviewDao.insert(review)
    }
}
