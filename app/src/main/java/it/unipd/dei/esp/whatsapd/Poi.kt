package it.unipd.dei.esp.whatsapd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poi_table")
class Poi(
    @PrimaryKey @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "photo_path") val photo_path: String,
    @ColumnInfo(name = "favourite") val favourite: Boolean,
    @ColumnInfo(name = "wheelchair_accessible") val wheelchair_accessible: Boolean,
    @ColumnInfo(name = "deaf_accessible") val deaf_accessible: Boolean,
    @ColumnInfo(name = "blind_accessible") val blind_accessible: Boolean
)