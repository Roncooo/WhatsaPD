package it.unipd.dei.esp.whatsapd.repository.database

import android.content.Context
import com.opencsv.CSVReader
import java.io.InputStream

/**
 * Provides static methods in order to parse CSV files into objects (Poi and Review).
 */
class CSVParser {
	companion object {
		
		/**
		 * Responsible of parsing an inputStream in csv format into a Mutable List of Pois.
		 */
		@JvmStatic
		fun poiParsing(inputStream: InputStream, context: Context): MutableList<Poi> {
			val poiList = mutableListOf<Poi>()
			
			CSVReader(inputStream.reader()).use { reader ->
				
				var line: Array<String?>?
				while (reader.readNext().also { line = it } != null) {
					val name = line?.get(0).toString()
					val latitude = line?.get(1)?.toDouble()!!
					val longitude = line?.get(2)?.toDouble()!!
					val description = line?.get(3).toString()
					val photoPath = line?.get(4).toString()
					val photoId = context.resources.getIdentifier(
						photoPath, "drawable", context.packageName
					)
					val favourite = line?.get(5).toBoolean()
					val wheelchairAccessible = line?.get(6).toBoolean()
					val deafAccessible = line?.get(7).toBoolean()
					val blindAccessible = line?.get(8).toBoolean()
					
					val poi = Poi(
						name,
						latitude,
						longitude,
						description,
						photoId,
						favourite,
						wheelchairAccessible,
						deafAccessible,
						blindAccessible
					)
					poiList.add(poi)
				}
			}
			
			return poiList
		}
		
		/**
		 * Responsible of parsing an inputStream in csv format into a Mutable List of Reviews.
		 */
		@JvmStatic
		fun reviewParsing(inputStream: InputStream): MutableList<Review> {
			val reviewList = mutableListOf<Review>()
			
			CSVReader(inputStream.reader()).use { reader ->
				var line: Array<String>?
				while (reader.readNext().also { line = it } != null) {
					
					val username = line?.get(0).toString()
					val poi = line?.get(1).toString()
					val rating = line?.get(2)?.toByte()!!
					val text = line?.get(3).toString()
					val date = Converters.stringToDate(line?.get(4).toString())!!
					
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
			
			return reviewList
		}
	}
}
