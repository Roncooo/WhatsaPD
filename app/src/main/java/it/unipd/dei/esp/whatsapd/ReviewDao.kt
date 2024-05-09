package it.unipd.dei.esp.whatsapd;

import androidx.room.Dao;
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query;


import kotlinx.coroutines.flow.Flow;

@Dao
interface ReviewDao {

    @Query("SELECT * FROM REVIEW_TABLE WHERE LOWER(POI)=LOWER(:poi) ORDER BY RATING ASC")
    fun getAllReviewsOfPoiByRating(poi: Poi): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(review: Review)

}
