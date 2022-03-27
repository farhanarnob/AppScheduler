package com.farhanarnob.appscheduler.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtility {
    const val WITH_SEC_DATE_FORMAT = "dd/MMM/yyyy hh:mm:ss aa"
    const val UTC = "UTC"
    /**
     * @param dateFormat    in which format, we have to provide date like MM-DD-YYYY
     * @param timesInMillis times in millis
     * @return formatted date in string like 12-02-2018
     */
    fun getTimeInString(dateFormat: String?, timesInMillis: Long): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val timeZone = TimeZone.getTimeZone(UTC)
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = timesInMillis
        return formatter.format(calendar.time)
    }
}