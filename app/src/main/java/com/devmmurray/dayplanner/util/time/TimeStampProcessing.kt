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

//        val correctFormat = when (flag) {
//            TimeFlags.FULL -> SimpleDateFormat("E, MMMM d hh:mm a")
//            TimeFlags.DAY -> SimpleDateFormat("EEEE")
//            TimeFlags.HOUR -> SimpleDateFormat("hh a")
//        }
        return stringFormatter(flag).format(timeStamp)
    }

    fun transformSystemTime(millis: Long, flag: TimeFlags): String {
        return stringFormatter(flag).format(millis)
    }

    private fun stringFormatter(flag: TimeFlags): SimpleDateFormat {

        return when (flag) {
            TimeFlags.FULL -> SimpleDateFormat("E, MMMM d h:mm a")
            TimeFlags.DAY -> SimpleDateFormat("EEEE")
            TimeFlags.HOUR -> SimpleDateFormat("hh a")
        }
    }

}