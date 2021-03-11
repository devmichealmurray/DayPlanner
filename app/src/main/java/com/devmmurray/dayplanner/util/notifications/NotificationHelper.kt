package com.devmmurray.dayplanner.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.ui.activity.SplashActivity

private const val TAG = "Notification Helper"
object NotificationHelper {

    fun createNotificationChannel(channelId: String, context: Context) {

            val notificationChannel = NotificationChannel(
                channelId,
                "Day Planner",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationChannel.apply {
                setShowBadge(true)
                description = "Events Notification"

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun sendNotification(context: Context) {
        val notificationIntent = Intent(context, SplashActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = context.let {
            NotificationCompat.Builder(it, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_search)
                .setContentTitle("Day Planner")
                .setContentText("Plan Your Day With Day Planner!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        }

        val notificationManagerCompat = context.let { NotificationManagerCompat.from(it) }
        notificationManagerCompat.notify(1, notification)
    }

}