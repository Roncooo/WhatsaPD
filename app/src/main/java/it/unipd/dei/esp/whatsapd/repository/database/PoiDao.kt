package it.unipd.dei.esp.whatsapd.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Provides methods to access Poi Data
 */
@Dao
interface PoiDao {

    /**
     * Returns all Pois in alphabetical order contained in the database
     */
    @Query("SELECT * FROM POI_TABLE ORDER BY NAME ASC")
    fun getAllPoisAlphabetized(): Flow<List<Poi>>

    /**
     * Returns all Pois contained in the database
     */
    @Query("SELECT * FROM POI_TABLE")
    fun getAllPois(): Flow<List<Poi>>

    @Query("SELECT * FROM POI_TABLE")
    fun getPoisUnordered(): List<Poi>

    /**
     * Returns alphabetical ordered Pois with favourite = 1 where 1 means true
     */
    @Query("SELECT * FROM POI_TABLE WHERE FAVOURITE=1 ORDER BY NAME ASC")
    fun getFavouritePoisAlphabetized(): Flow<List<Poi>>

    /**
     * Searches searchedName inside the names of the Pois.
     * Both the Poi.name and searchedName are put to lowercase so that the case is ignored
     */
    @Query("SELECT * FROM POI_TABLE WHERE LOWER(NAME) LIKE '%' || LOWER(:searchedName) || '%' ORDER BY NAME ASC")
    fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>>

    /**
     * Returns the Poi whose name corresponds to searchedName
     */
    @Query("SELECT * FROM POI_TABLE WHERE NAME = :searchedName")
    fun getPoiByName(searchedName: String): LiveData<Poi>

    /**
     * Inserts poi into the database, if conflicts happen we ignore them
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(poi: Poi)

    @Query("UPDATE POI_TABLE SET FAVOURITE = :newFavouriteValue WHERE name = :poiName")
    suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean)

    /**
     * Deletes all Pois from the poi table in the database
     */
    @Query("DELETE FROM POI_TABLE")
    suspend fun deleteAll()
}