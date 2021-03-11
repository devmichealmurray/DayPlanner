package com.devmmurray.dayplanner.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.ui.activity.MainActivity

object NotificationHelper {

//    fun createNotificationChannel(
//        context: Context, importance: Int, showBadge: Boolean,
//        name: String, description: String
//    ) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "${context.packageName}-$name"
//            val channel = NotificationChannel(channelId, name, importance)
//            channel.description = description
//            channel.setShowBadge(showBadge)
//            val notificationManager: NotificationManager =
//                context.getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    fun createNotificationChannel(channelId: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel = NotificationChannel(
                channelId,
                "Day Planner",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationChannel.apply {
                setShowBadge(true)
                description = "Events Notification"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun createNotification(
        context: Context,
        title: String,
        message: String,
        autoCancel: Boolean
    ) {

        // Creates the unique channel id
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
        //Builds the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_search)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(autoCancel)
            // Creates the intent to launch Main Activity
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Wraps the intent as a Pending Intent
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            // Attaches Intent to the notification
            setContentIntent(pendingIntent)

        }
        // Uses context to reference the app's notification manager
        val notificationManager = NotificationManagerCompat.from(context)
        // notify on the manager passing an identifier and the notification
        notificationManager.notify(1001, notificationBuilder.build())
    }

}