package com.yusuf.paparafinalcase.core.constants.utils.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.yusuf.paparafinalcase.R

object NotificationHelper {
    private const val CHANNEL_ID = "daily_recommendation_channel"
    private const val NOTIFICATION_ID = 1

    fun showNotification(context: Context, title: String, content: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Recommendations",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_splash)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
