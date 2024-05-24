package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Provides methods to access [Poi] data.
 */
@Dao
interface PoiDao {
	
	/**
	 * Returns all [Poi]s in alphabetical order contained in the database.
	 */
	@Query("SELECT * FROM POI_TABLE ORDER BY NAME ASC")
	fun getAllPoisAlphabetized(): Flow<List<Poi>>
	
	/**
	 * Returns all [Poi]s contained in the database without ordering them.
	 */
	@Query("SELECT * FROM POI_TABLE")
	fun getAllPois(): Flow<List<Poi>>
	
	/**
	 * Returns alphabetical ordered [Poi]s with favourite = 1 where 1 means true.
	 */
	@Query("SELECT * FROM POI_TABLE WHERE FAVOURITE=1 ORDER BY NAME ASC")
	fun getFavouritePoisAlphabetized(): Flow<List<Poi>>
	
	/**
	 * Searches [searchedName] inside the names of the [Poi]s.
	 * Both the [Poi.name] and [searchedName] are put to lowercase so that the case is ignored.
	 */
	@Query("SELECT * FROM POI_TABLE WHERE LOWER(NAME) LIKE '%' || LOWER(:searchedName) || '%' ORDER BY NAME ASC")
	fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>>
	
	/**
	 * Returns the [Poi] whose name corresponds to [searchedName].
	 */
	@Query("SELECT * FROM POI_TABLE WHERE NAME = :searchedName")
	fun getPoiByName(searchedName: String): Flow<Poi>
	
	/**
	 * Inserts [poi] into the database, if conflict happen the insertion is ignored.
	 */
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(poi: Poi)
	
	/**
	 * Sets the value of favourite attribute for the [Poi] with name [poiName]. If the name is wrong
	 * nothing happens.
	 */
	@Query("UPDATE POI_TABLE SET FAVOURITE = :newFavouriteValue WHERE name = :poiName")
	suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean)
	
	/**
	 * Deletes all [Poi]s from the poi table in the database.
	 */
	@Query("DELETE FROM POI_TABLE")
	suspend fun deleteAll()
}