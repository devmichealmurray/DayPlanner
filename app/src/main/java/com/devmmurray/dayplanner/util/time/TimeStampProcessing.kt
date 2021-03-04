package com.devmmurray.dayplanner.util.time

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeStampProcessing {

    @SuppressLint("SimpleDateFormat")
    fun transformUTCTime(utcTime: Long, flag: TimeFlags): String {
        val timeStamp = Date(
            TimeUnit.MILLISECONDS
                .convert(utcTime, TimeUnit.SECONDS)
        ).time

        return stringFormatter(flag).format(timeStamp)
    }

    fun transformSystemTime(millis: Long, flag: TimeFlags): String =
        stringFormatter(flag).format(millis)

    @SuppressLint("SimpleDateFormat")
    fun transformDateStringToMillis(timeString: String): Long {
        val sdf = SimpleDateFormat("d-M-yyyy HH:mm")
        val convertDate = sdf.parse(timeString)
        return convertDate!!.time
    }

    fun todaysDate(): String {
        val time = System.currentTimeMillis()
        val sdf = SimpleDateFormat("M-d-yyyy")
        return sdf.format(time)
    }

    private fun stringFormatter(flag: TimeFlags): SimpleDateFormat {

        return when (flag) {
            TimeFlags.FULL -> SimpleDateFormat("E, MMMM d h:mm a")
            TimeFlags.DAY -> SimpleDateFormat("EEEE")
            TimeFlags.HOUR -> SimpleDateFormat("h a")
        }
    }



}