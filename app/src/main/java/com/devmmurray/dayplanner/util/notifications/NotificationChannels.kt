package com.devmmurray.dayplanner.util.notifications

import android.app.Application

const val NOTIFICATION_CHANNEL_ID = "1"
private const val TAG = "NotificationChannels"

class NotificationChannels : Application() {
    private lateinit var instance: NotificationChannels

    override fun onCreate() {
        super.onCreate()
        instance = this

        NotificationHelper.createNotificationChannel(NOTIFICATION_CHANNEL_ID, this)
    }




}