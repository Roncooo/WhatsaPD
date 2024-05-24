package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Provides static methods in order to convert the object [Date] into a [String] and vice-versa.
 */
class Converters {
	companion object {
		
		/**
		 * The only format in which [Date]s are represented in the app: dd/MM/yyy
		 */
		var dateFormat = SimpleDateFormat(
			"dd/MM/yyyy",
			Locale.getDefault()
		)
		
		/**
		 * Converts a string [value] into a [Date] object using [dateFormat].
		 * Used to retrieve [Date] objects from the strings written in the database and in the CSV files.
		 */
		@TypeConverter
		@JvmStatic
		fun stringToDate(value: String?): Date? {
			return value?.let { dateFormat.parse(it) }
		}
		
		/**
		 * Converts a [Date] object into a string using [dateFormat].
		 * Used to write in the database and to show the [Date] objects to the user.
		 */
		@TypeConverter
		@JvmStatic
		fun dateToString(date: Date?): String? {
			return date?.let { dateFormat.format(it) }
		}
	}
}