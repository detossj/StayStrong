package com.deto.staystrong.ui.Notificacion

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
//notificaciones
class DailyReminderReceiver : BroadcastReceiver() {
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

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.deto.staystrong.R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Hora de entrenar!")
            .setContentText("No olvides registrar tu entrenamiento hoy.")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}