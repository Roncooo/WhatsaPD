package it.unipd.dei.esp.whatsapd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

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
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "poi") val poi: String
)
