package com.devmmurray.dayplanner.util.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val TAG = "Alarm Receiver"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            NotificationHelper.sendNotification(context)
        }
    }

}
