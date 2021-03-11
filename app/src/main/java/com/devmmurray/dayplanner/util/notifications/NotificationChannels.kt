package com.devmmurray.dayplanner.util.notifications

import android.app.Application
import android.util.Log

const val NOTIFICATION_CHANNEL_ID = "1"
private const val TAG = "NotificationChannels"

class NotificationChannels : Application() {
    private lateinit var instance: NotificationChannels

    override fun onCreate() {
        super.onCreate()
        instance = this

        Log.d(TAG, "================= Notification Channels Called ========================")
        NotificationHelper.createNotificationChannel(NOTIFICATION_CHANNEL_ID, this)
    }




}