package it.unipd.dei.esp.whatsapd

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Converters {
    companion object {
        var dateFormat = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: String?): Date? {
            return value?.let { dateFormat.parse(it) }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): String? {
            return date?.let { dateFormat.format(it) }
        }
    }
}