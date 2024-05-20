package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

/*
parentColumns is the name of the column of the other table
childColumns is the name of the column of this table
onDelete CASCADE means that when a Poi is deleted, all its Reviews will be deleted
 */
@Entity(
    tableName = "review_table", foreignKeys = [ForeignKey(
        entity = Poi::class,
        parentColumns = ["name"],
        childColumns = ["poi"],
        onDelete = ForeignKey.CASCADE
    )]
)

class Review(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "poi") val poi: String,
    @ColumnInfo(name = "rating") val rating: Byte,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "date") val date: Date = Date(),
    // autoGenerate manages the autoincrementing id so we don't have to
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
