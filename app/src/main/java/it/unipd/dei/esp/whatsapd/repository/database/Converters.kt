package it.unipd.dei.esp.whatsapd.repository.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Provides static methods in order to convert the object Date into a string and viceversa
 */
class Converters {
    companion object {
        var dateFormat = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

        /**
         * Converts a string into an object Date
         */
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: String?): Date? {
            return value?.let { dateFormat.parse(it) }
        }

        /**
         * Converts an object Date into a string
         */
        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): String? {
            return date?.let { dateFormat.format(it) }
        }
    }
}