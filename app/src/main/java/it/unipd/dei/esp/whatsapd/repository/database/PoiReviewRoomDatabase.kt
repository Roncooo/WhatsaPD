package it.unipd.dei.esp.whatsapd.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import it.unipd.dei.esp.whatsapd.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Defines the database containing Poi and review records, extends class RoomDatabase
 */
@Database(entities = [Poi::class, Review::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PoiReviewRoomDatabase : RoomDatabase() {
	
	abstract fun poiDao(): PoiDao
	abstract fun reviewDao(): ReviewDao
	
	companion object {
		@Volatile
		private var INSTANCE: PoiReviewRoomDatabase? = null
		
		fun getDatabase(
			context: Context, scope: CoroutineScope
		): PoiReviewRoomDatabase {
			// if the INSTANCE is not null, then return it,
			// if it is, then create the database
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					PoiReviewRoomDatabase::class.java,
					"poi_review_database"
				).fallbackToDestructiveMigration()
					// quando parte il db non deve salvare i dati della versione precedente
					.addCallback(PoiReviewDatabaseCallback(scope, context)).build()
				INSTANCE = instance
				// return instance
				instance
			}
		}
	}
	
	
	// necessary in order to call fun populateDatabase(...) inside a coroutine
	private class PoiReviewDatabaseCallback(
		private val scope: CoroutineScope, private val context: Context
	) : Callback() {
		
		override fun onCreate(db: SupportSQLiteDatabase) {
			super.onCreate(db)
			INSTANCE?.let { database ->
				scope.launch(Dispatchers.IO) {
					populateDatabase(database.poiDao(), database.reviewDao(), context)
				}
			}
		}
		
		/**
		 * Called the first time the database is initiated.
		 */
		suspend fun populateDatabase(poiDao: PoiDao, reviewDao: ReviewDao, context: Context) {
			// Delete all content here.
			poiDao.deleteAll()
			
			val poiListInputStream = context.resources.openRawResource(R.raw.pois)
			val poiList = poiListInputStream.let { CSVParser.poiParsing(it, context) }
			while (poiList.isNotEmpty()) {
				val poi = poiList.removeAt(0)
				poiDao.insert(poi)
			}
			
			val reviewListInputStream = context.resources.openRawResource(R.raw.reviews)
			val reviewList = reviewListInputStream.let { CSVParser.reviewParsing(it) }
			while (reviewList.isNotEmpty()) {
				val review = reviewList.removeAt(0)
				reviewDao.insert(review)
			}
		}
	}
}



