package com.farhanarnob.appscheduler.util

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateUtility {
    const val DEFAULT_DATE = 0L
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

    fun timePicker(
        title: String,
        context: Context,
        date: Long,
        onTimeSetListener: TimePickerDialog.OnTimeSetListener,
    ): TimePickerDialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val timePicker = TimePickerDialog(
            context,
            AlertDialog.THEME_HOLO_LIGHT,
            onTimeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.setTitle(title)
        timePicker.setCancelable(false)
        timePicker.show()
        return timePicker
    }

    fun getTimeFromTimePicker(time: Long, hourOfDay: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE,minute)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        return calendar.timeInMillis
    }

    fun addADay(scheduledTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = scheduledTime
        calendar.add(Calendar.HOUR_OF_DAY,24)
        return calendar.timeInMillis
    }

}