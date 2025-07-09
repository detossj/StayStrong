package com.deto.staystrong.ui.notification


import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.deto.staystrong.MainActivity
import com.deto.staystrong.R

class DailyReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationScheduler.scheduleDailyReminder(context)
            return
        }

        val channelId = "daily_reminder_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorio diario",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intentToOpenApp = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentToOpenApp,
            PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_staystrong)
            .setContentTitle("¡Hora de entrenar!")
            .setContentText("No olvides registrar tu entrenamiento hoy.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }

}