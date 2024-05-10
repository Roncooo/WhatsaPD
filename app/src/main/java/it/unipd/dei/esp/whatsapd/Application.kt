package it.unipd.dei.esp.whatsapd

import android.app.Application
import com.example.android.roomwordssample.PoiReviewRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { PoiReviewRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PoiReviewRepository(database.poiDao(), database.reviewDao()) }
}
