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
 * Defines the database containing [Poi] and [Review] records, extends class [RoomDatabase].
 */
@Database(entities = [Poi::class, Review::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PoiReviewRoomDatabase : RoomDatabase() {
	
	abstract fun poiDao(): PoiDao
	abstract fun reviewDao(): ReviewDao
	
	companion object {
		@Volatile
		private var INSTANCE: PoiReviewRoomDatabase? = null
		
		/**
		 * Singleton pattern: if [INSTANCE] is not null then it is returned, if it is null then
		 * it is initialized. This also works as a proxy pattern because [PoiReviewRoomDatabase]
		 * object can be created without actually initializing the database, this operation can
		 * be done at a later time by invoking [getDatabase].
		 */
		fun getDatabase(
			context: Context, scope: CoroutineScope
		): PoiReviewRoomDatabase {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					PoiReviewRoomDatabase::class.java,
					"poi_review_database"
				).fallbackToDestructiveMigration()
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



