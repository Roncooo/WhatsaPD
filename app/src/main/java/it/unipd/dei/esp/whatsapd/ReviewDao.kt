package it.unipd.dei.esp.whatsapd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT * FROM REVIEW_TABLE WHERE LOWER(POI)=LOWER(:poi_name) ORDER BY RATING DESC")
    fun getAllReviewsOfPoiByRating(poi_name: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(review: Review)

}
