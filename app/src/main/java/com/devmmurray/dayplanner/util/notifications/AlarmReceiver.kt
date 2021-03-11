package com.devmmurray.dayplanner.util.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devmmurray.dayplanner.ui.activity.SplashActivity
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        shouldNotifyToday()
    }

    private fun shouldNotifyToday() {
        val today = TimeStampProcessing.todaysDate(TimeFlags.DATE_ID)
        SplashActivity().checkDataBaseForEvents(today)
    }
}