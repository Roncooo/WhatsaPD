package it.unipd.dei.esp.whatsapd

import android.content.Context
import com.opencsv.CSVReader
import java.io.IOException

class CSVParser {
companion object {
    fun fileParsing(context: Context): MutableList<Poi> {
        val poiList = mutableListOf<Poi>()
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
                    val photo_id =
                        context.resources.getIdentifier(
                            photo_path,
                            "drawable",
                            context.packageName
                        )
                    val favourite = line?.get(5).toBoolean()
                    val wheelchair_accessible = line?.get(6).toBoolean()
                    val deaf_accessible = line?.get(7).toBoolean()
                    val blind_accessible = line?.get(8).toBoolean()

                    val poi = Poi(
                        name,
                        latitude,
                        longitude,
                        description,
                        photo_id,
                        favourite,
                        wheelchair_accessible,
                        deaf_accessible,
                        blind_accessible
                    )
                    //poiDao.insert(poi)
                    poiList.add(poi)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return poiList
    }
}
}
