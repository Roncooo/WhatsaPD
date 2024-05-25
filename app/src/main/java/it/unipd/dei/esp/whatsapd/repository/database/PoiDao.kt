package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * [PoiDao] (Data Access Object) provides methods to access [Poi] data.
 */
@Dao
interface PoiDao {
	
	/**
	 * Returns all [Poi]s contained in the database without a specific order.
	 */
	@Query("SELECT * FROM POI_TABLE")
	fun getAllPois(): Flow<List<Poi>>
	
	/**
	 * Returns all [Poi]s in alphabetical order contained in the database.
	 */
	@Query("SELECT * FROM POI_TABLE ORDER BY NAME ASC")
	fun getAllPoisAlphabetized(): Flow<List<Poi>>
	
	/**
	 * Returns alphabetical ordered [Poi]s with favourite = 1 where 1 means true.
	 */
	@Query("SELECT * FROM POI_TABLE WHERE FAVOURITE=1 ORDER BY NAME ASC")
	fun getFavouritePoisAlphabetized(): Flow<List<Poi>>
	
	/**
	 * Searches [searchName] inside the names of the [Poi]s in the database.
	 * Both the [Poi.name] and [searchName] are put to lowercase so that the capitalization is ignored.
	 */
	@Query("SELECT * FROM POI_TABLE WHERE LOWER(NAME) LIKE '%' || LOWER(:searchName) || '%' ORDER BY NAME ASC")
	fun getPoisByNameAlphabetized(searchName: String): Flow<List<Poi>>
	
	/**
	 * Returns the [Poi] (if it exists) whose name corresponds to [searchedName].
	 */
	@Query("SELECT * FROM POI_TABLE WHERE NAME = :searchedName")
	fun getPoiByName(searchedName: String): Flow<Poi>
	
	/**
	 * Sets the value of favourite attribute for the [Poi] with name [poiName]. If the name is wrong
	 * nothing happens.
	 */
	@Query("UPDATE POI_TABLE SET FAVOURITE = :newFavouriteValue WHERE name = :poiName")
	suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean)
	
	/**
	 * Inserts [poi] into the database, if conflict happen the insertion is ignored.
	 */
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(poi: Poi)
	
	/**
	 * Deletes all [Poi]s from the poi table in the database.
	 */
	@Query("DELETE FROM POI_TABLE")
	suspend fun deleteAll()
}
