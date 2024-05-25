package it.unipd.dei.esp.whatsapd

import android.app.Application
import it.unipd.dei.esp.whatsapd.repository.PoiReviewRepository
import it.unipd.dei.esp.whatsapd.repository.database.PoiReviewRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application : Application() {
	private val applicationScope = CoroutineScope(SupervisorJob())
	val database by lazy { PoiReviewRoomDatabase.getDatabase(this, applicationScope) }
	val repository by lazy { PoiReviewRepository(database.poiDao(), database.reviewDao()) }
}
