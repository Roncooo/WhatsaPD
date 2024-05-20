package it.unipd.dei.esp.whatsapd.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                    .addCallback(PoiReviewDatabaseCallback(scope, context))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }


    // serve solo per chiamare popuateDatabase all'interno di una coroutine
    private class PoiReviewDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.poiDao(), database.reviewDao(), context)
                }
            }
        }

        // For the first time the app is executed
        suspend fun populateDatabase(poiDao: PoiDao, reviewDao: ReviewDao, context: Context ) {
            // Delete all content here.
            poiDao.deleteAll()

            val poiList = CSVParser.poiParsing(context)

            while (poiList.isNotEmpty()) {
                val poi = poiList.removeAt(0)
                poiDao.insert(poi)
            }

            val reviewList = CSVParser.reviewParsing(context)

            while (reviewList.isNotEmpty()) {
                val review = reviewList.removeAt(0)
                reviewDao.insert(review)

            }
            }

        }


    }



