package com.itis.itis_android_tasks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHandler {

    fun createNotification(context: Context,
                           id: Int,
                           importance: Int,
                           privacy: Int,
                           isSeeMoreFeatureEnabled: Boolean,
                           areAppLaunchingFeaturesEnabled: Boolean,
                           title: String,
                           text: String) {

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    context.getString(R.string.first_notification_channel_id),
                    context.getString(R.string.main_notification_channel_name),
                    importance
                )
                manager.createNotificationChannel(channel)
            }

            val intentForLaunching = Intent(context, MainActivity::class.java)

            val pendingIntentForLaunching = PendingIntent.getActivity(
                context,
                101,
                intentForLaunching,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )


            val notification = NotificationCompat.Builder(context, context.getString(R.string.first_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setVisibility(privacy)
                .setContentIntent(pendingIntentForLaunching)

            if (isSeeMoreFeatureEnabled) {
                notification.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(text))
            }

            if (areAppLaunchingFeaturesEnabled) {

                val intentForToast = Intent(context, MainActivity::class.java).putExtra(ParamsKey.MAKE_A_TOAST_KEY, true)
                val pendingIntentForToast = PendingIntent.getActivity(
                    context,
                    102,
                    intentForToast,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

                val intentForSettings = Intent(context, MainActivity::class.java).putExtra(ParamsKey.GO_TO_NOTIFICATION_SETTINGS_KEY, true)
                val pendingIntentForSettings = PendingIntent.getActivity(
                    context,
                    103,
                    intentForSettings,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )


                notification.addAction(R.drawable.ic_toast, context.getString(R.string.toast), pendingIntentForToast)
                notification.addAction(R.drawable.ic_notifications, context.getString(R.string.settings), pendingIntentForSettings)
            }


            manager.notify(id, notification.build())
        }
    }
}
