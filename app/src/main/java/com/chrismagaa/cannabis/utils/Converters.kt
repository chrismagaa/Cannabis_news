package com.chrismagaa.cannabis.utils

import com.tickaroo.tikxml.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class XMLDateConverter : TypeConverter<Date?> {
  private val formatter: SimpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US)

  @Throws(Exception::class)
  override fun read(value: String?): Date {
    return formatter.parse(value)
  }

  @Throws(Exception::class)
  override fun write(value: Date?): String {
    return formatter.format(value)
  }
}

class RoomDateConverter {
  @androidx.room.TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @androidx.room.TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time?.toLong()
  }
}