package com.example.android.roomwordssample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.opencsv.CSVReader
import it.unipd.dei.esp.whatsapd.Converters
import it.unipd.dei.esp.whatsapd.Poi
import it.unipd.dei.esp.whatsapd.PoiDao
import it.unipd.dei.esp.whatsapd.R
import it.unipd.dei.esp.whatsapd.Review
import it.unipd.dei.esp.whatsapd.ReviewDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date


@Database(entities = [Poi::class, Review::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PoiReviewRoomDatabase : RoomDatabase() {

    abstract fun poiDao(): PoiDao
    abstract fun reviewDao(): ReviewDao

    // The companion object makes the variable and the function static
    companion object {

        @Volatile
        private var INSTANCE: PoiReviewRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PoiReviewRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PoiReviewRoomDatabase::class.java,
                    "poi_review_database"
                )
                    .fallbackToDestructiveMigration()       // quando parte il db non deve salvare i dati della versione precedente
                    .addCallback(PoiReviewDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    // serve solo per chiamare popuateDatabase all'interno di una coroutine
    private class PoiReviewDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.poiDao(), database.reviewDao())
                }
            }
        }

        // For the first time the app is executed
        suspend fun populateDatabase(poiDao: PoiDao, reviewDao: ReviewDao) {
            // Delete all content here.
            poiDao.deleteAll()

            try {
                val inputStream = this.javaClass.classLoader.getResourceAsStream("res/raw/pois.csv")

                CSVReader(inputStream.reader()).use { reader ->
                    var line: Array<String?>?
                    while (reader.readNext().also { line = it } != null) {
                        val name = line?.get(0).toString()
                        val latitude = line?.get(1)?.toDouble()!!
                        val longitude = line?.get(2)?.toDouble()!!
                        val description = line?.get(3).toString()
                        val photo_path = line?.get(4)
                            .toString()
                        val favourite = line?.get(5).toBoolean()
                        val wheelchair_accessible = line?.get(6).toBoolean()
                        val deaf_accessible = line?.get(7).toBoolean()
                        val blind_accessible = line?.get(8).toBoolean()

                        val poi = Poi(
                            name,
                            latitude,
                            longitude,
                            description,
                            R.drawable.prato_della_valle,   // todo change to string
                            favourite,
                            wheelchair_accessible,
                            deaf_accessible,
                            blind_accessible
                        )
                        poiDao.insert(poi)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            TODO("Same thing with reviews. Use Converters().fromTimestamp to read the string")


            // Add sample pois
            var poi = Poi(
                "Orto Botanico",
                0.1,
                0.1,
                "È l'orto più bello d'europa e bla bla bla",
                R.drawable.orto_botanico,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
            poi = Poi(
                "Caffe pedrocchi",
                0.1,
                0.1,
                "È il caffe più bello d'europa e bla bla bla",
                R.drawable.caffe_pedrocchi,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
            poi = Poi(
                "Cappella degli scrovegni",
                0.1,
                0.1,
                "È la cappella più bella d'europa e bla bla bla",
                R.drawable.cappella_degli_scrovegni,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
            poi = Poi(
                "Porta portello",
                45.0,
                11.0,
                "È la porta più bella d'europa e bla bla bla",
                R.drawable.porta_portello,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
            poi = Poi(
                "Torre dell'orologio",
                45.0,
                11.0,
                "È l'orologio più bello d'europa e bla bla bla",
                R.drawable.torre_dell_orologio,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
            poi = Poi(
                "DEI",
                45.0,
                11.0,
                "È il dipartimento più bello d'europa e bla bla bla",
                R.drawable.facciata_dei,
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)

            var review = Review(
                username = "Pippo",
                date = Date(),
                poi = "Prato della valle",
                rating = 5,
                text = "Molto bello rilassarsi sotto agli alberi"
            )
            reviewDao.insert(review)

            review = Review(
                username = "Pluto",
                date = Date(1912, 11, 4),
                poi = "Prato della valle",
                rating = 4,
                text = "Che bello il mercato del mercoledì"
            )
            reviewDao.insert(review)

            review = Review(
                username = "Paperino",
                date = Date(2034, 2, 17),
                poi = "Porta portello",
                rating = 5,
                text = "Per me un golosino"
            )
            reviewDao.insert(review)

        }
    }


}


