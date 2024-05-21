package it.unipd.dei.esp.whatsapd.repository.database

import android.content.Context
import com.opencsv.CSVReader
import java.io.IOException

/**
 * Provides static methods in order to parse CSV files
 */
class CSVParser {
    companion object {

        /**
         * Responsible of parsing poi.csv file, returns a Mutable List of Points of Interest
         */
        @JvmStatic
        fun poiParsing(context: Context): MutableList<Poi> {
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
                        poiList.add(poi)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return poiList
        }

        /**
         * Responsible of parsing reviews.csv file, returns a Mutable List of Reviews
         */
        @JvmStatic
        fun reviewParsing(context: Context): MutableList<Review> {
            val reviewList = mutableListOf<Review>()
            try {

                val inputStream =
                    this.javaClass.classLoader.getResourceAsStream("res/raw/reviews.csv")

                CSVReader(inputStream.reader()).use { reader ->
                    var line: Array<String>?
                    while (reader.readNext().also { line = it } != null) {

                        val username = line?.get(0).toString()
                        val poi = line?.get(1).toString()
                        val rating = line?.get(2)?.toByte()!!
                        val text = line?.get(3).toString()
                        val date = Converters.fromTimestamp(line?.get(4).toString())!!

                        val review = Review(
                            username,
                            poi,
                            rating,
                            text,
                            date,
                        )

                        reviewList.add(review)
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return reviewList
        }
    }
}
