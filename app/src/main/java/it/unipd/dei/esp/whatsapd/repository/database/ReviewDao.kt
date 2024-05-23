package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


import kotlinx.coroutines.flow.Flow

/**
 * Provides methods to access Review data.
 */
@Dao
interface ReviewDao {
	
	/**
	 * Returns all reviews ordered by descending rating.
	 */
	@Query("SELECT * FROM REVIEW_TABLE WHERE LOWER(POI)=LOWER(:poiName) ORDER BY RATING DESC")
	fun getAllReviewsOfPoiByRating(poiName: String): Flow<List<Review>>
	
	/**
	 * Inserts review. Conflicts shouldn't happen because the primary key is autogenerated but
	 * if conflict happen the insertion is ignored.
	 */
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(review: Review)
	
}
