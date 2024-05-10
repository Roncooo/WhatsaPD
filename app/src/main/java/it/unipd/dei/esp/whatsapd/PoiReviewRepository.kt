package it.unipd.dei.esp.whatsapd

import kotlinx.coroutines.flow.Flow

class PoiReviewRepository(private val poiDao: PoiDao, private val reviewDao: ReviewDao) {

    // get all pois
    val allPoi: Flow<List<Poi>> = poiDao.getAllPois()

    // get all pois alphabetized
    val allPoiAlphabetized: Flow<List<Poi>> = poiDao.getFavouritePoisAlphabetized()

    // define a getter function with a param
    fun getAllReviewsOfPoiByRating(poi: Poi): Flow<List<Review>> {
        val allReviews: Flow<List<Review>> = reviewDao.getAllReviewsOfPoiByRating(poi)
        return allReviews
    }


}