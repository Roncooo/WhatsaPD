package com.example.android.roomwordssample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import it.unipd.dei.esp.whatsapd.Converters
import it.unipd.dei.esp.whatsapd.Poi
import it.unipd.dei.esp.whatsapd.PoiDao
import it.unipd.dei.esp.whatsapd.Review
import it.unipd.dei.esp.whatsapd.ReviewDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Poi::class, Review::class], version = 1, exportSchema = false)
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
                    populateDatabase(database.poiDao())
                }
            }
        }

        // Questo serve per la prima volta che eseguo
        suspend fun populateDatabase(poiDao: PoiDao) {
            // Delete all content here.
            poiDao.deleteAll()

            // Add sample words.
            var poi = Poi(
                "Prato della valle",
                45.3984171,
                11.8765285,
                "Prato della valle è la piazza più grande d'europa e bla bla bla",
                "prato_della_valle",
                false,
                true,
                true,
                true
            )
            poiDao.insert(poi)

            poi = Poi(
                "Orto Botanico",
                45.3984171,
                11.8765285,
                "È l'orto più bello d'europa e bla bla bla",
                "orto_botanico",
                true,
                true,
                true,
                true
            )
            poiDao.insert(poi)
        }
    }


}


