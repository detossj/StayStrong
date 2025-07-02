package com.deto.staystrong

import android.Manifest
import android.app.Application
import com.deto.staystrong.data.AppContainer
import com.deto.staystrong.data.AppDataContainer
import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission

import com.deto.staystrong.ui.notification.NotificationScheduler

class StayStrong : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        scheduleDailyReminderIfNecessary()
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun scheduleDailyReminderIfNecessary() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isReminderScheduled = prefs.getBoolean("is_daily_reminder_scheduled", false)

        if (!isReminderScheduled) {
            val scheduled = NotificationScheduler.scheduleDailyReminder(applicationContext)
            prefs.edit().putBoolean("is_daily_reminder_scheduled", scheduled).apply()
            if (!scheduled) {
                prefs.edit().putBoolean("exact_alarm_permission_needed", true).apply()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                if (!alarmManager.canScheduleExactAlarms()) {
                    prefs.edit().putBoolean("exact_alarm_permission_needed", true).apply()
                }
            }
        }
    }
}
