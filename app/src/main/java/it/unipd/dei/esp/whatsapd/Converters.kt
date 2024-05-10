package it.unipd.dei.esp.whatsapd

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Converters {

    var dateFormat = SimpleDateFormat(
        "d MMM yyyy",
        Locale.getDefault()
    )

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { dateFormat.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }
}