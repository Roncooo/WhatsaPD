package it.unipd.dei.esp.whatsapd.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {
    @Query("SELECT * FROM POI_TABLE ORDER BY NAME ASC")
    fun getAllPoisAlphabetized(): Flow<List<Poi>>

    @Query("SELECT * FROM POI_TABLE")
    fun getAllPois(): Flow<List<Poi>>

    // 1 means true
    @Query("SELECT * FROM POI_TABLE WHERE FAVOURITE=1 ORDER BY NAME ASC")
    fun getFavouritePoisAlphabetized(): Flow<List<Poi>>

    // Searches searchedName inside the names of the Pois. For example if you search "della" you
    // should get the poi "Prato della valle".
    // Both the Poi.name and searchedName are put to lowercase so that the case is ignored
    @Query("SELECT * FROM POI_TABLE WHERE LOWER(NAME) LIKE '%' || LOWER(:searchedName) || '%' ORDER BY NAME ASC")
    fun getPoisByNameAlphabetized(searchedName: String): Flow<List<Poi>>

    @Query("SELECT * FROM POI_TABLE WHERE NAME = :searchedName")
    fun getPoiByName(searchedName: String): LiveData<Poi>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(poi: Poi)

    @Query("UPDATE POI_TABLE SET FAVOURITE = :newFavouriteValue WHERE name = :poiName")
    suspend fun updateFavourite(poiName: String, newFavouriteValue: Boolean)

    @Query("DELETE FROM POI_TABLE")
    suspend fun deleteAll()
}