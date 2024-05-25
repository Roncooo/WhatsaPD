package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * In the contest of the application, a [Poi] (Point of interest) is a place, building or monument
 * that tourists can see around the city. In a different contest this could easily be a restaurant,
 * bar, hotel or whatever you would like to map and review in a city. The class also defines the
 * table "Poi" in the database where each entry represents a different [Poi].
 */
@Entity(tableName = "poi_table")
open class Poi(
	@PrimaryKey @ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "latitude") val latitude: Double,
	@ColumnInfo(name = "longitude") val longitude: Double,
	@ColumnInfo(name = "description") val description: String,
	@ColumnInfo(name = "photo_path") val photoId: Int,
	@ColumnInfo(name = "favourite") var favourite: Boolean,
	@ColumnInfo(name = "wheelchair_accessible") val wheelchairAccessible: Boolean,
	@ColumnInfo(name = "deaf_accessible") val deafAccessible: Boolean,
	@ColumnInfo(name = "blind_accessible") val blindAccessible: Boolean
) : Serializable
