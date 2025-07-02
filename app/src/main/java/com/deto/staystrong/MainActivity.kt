package com.deto.staystrong

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.deto.staystrong.ui.Notificacion.NotificationScheduler
import com.deto.staystrong.ui.theme.StayStrongTheme
import androidx.core.content.edit


class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                scheduleExactAlarmPermissionCheck()
            }
        }

    private val requestExactAlarmPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val scheduled = NotificationScheduler.scheduleDailyReminder(applicationContext)
                getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit() {
                        putBoolean("is_daily_reminder_scheduled", scheduled)
                            .putBoolean("exact_alarm_permission_needed", !scheduled)
                    }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        askNotificationPermission()
        scheduleExactAlarmPermissionCheck()

        setContent {
            StayStrongTheme {
                Navigation()
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun scheduleExactAlarmPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.data = Uri.fromParts("package", packageName, null)
                requestExactAlarmPermissionLauncher.launch(intent)
            } else {
                val scheduled = NotificationScheduler.scheduleDailyReminder(applicationContext)
                getSharedPreferences("app_prefs", MODE_PRIVATE)
                    .edit() {
                        putBoolean("is_daily_reminder_scheduled", scheduled)
                            .putBoolean("exact_alarm_permission_needed", !scheduled)
                    }
            }
        } else {
            val scheduled = NotificationScheduler.scheduleDailyReminder(applicationContext)
            getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit() {
                    putBoolean("is_daily_reminder_scheduled", scheduled)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("exact_alarm_permission_needed", false)) {
            scheduleExactAlarmPermissionCheck()
        }
    }
}
