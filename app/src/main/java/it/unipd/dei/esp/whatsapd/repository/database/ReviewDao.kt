package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


import kotlinx.coroutines.flow.Flow

/**
 * Provides methods to access Review data
 */
@Dao
interface ReviewDao {

    /**
     * Returns all reviews in order of decreasing rating
     */
    @Query("SELECT * FROM REVIEW_TABLE WHERE LOWER(POI)=LOWER(:poi_name) ORDER BY RATING DESC")
    fun getAllReviewsOfPoiByRating(poi_name: String): Flow<List<Review>>

    /**
     * Inserts review, if conflicts happen we ignore them
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(review: Review)

}
