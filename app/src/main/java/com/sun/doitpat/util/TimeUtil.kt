package com.sun.doitpat.util

import java.util.*

object TimeUtils {

    fun timeToString(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH).plus(1)
        val year = calendar.get(Calendar.YEAR)
        val time = String.format("%02d:%02d", hour, minute)
        return "$time, $day-$month-$year"
    }

}
