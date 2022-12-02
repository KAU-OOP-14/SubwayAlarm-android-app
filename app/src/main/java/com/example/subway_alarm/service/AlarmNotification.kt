package com.example.subway_alarm.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.di.SubwayAlarmApp
import com.example.subway_alarm.ui.activities.MainActivity

object AlarmNotification: Notification() {

    fun createAlarmNotification(context: Context): NotificationCompat.Builder {
        val contentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        //알람 해제를 위한 intent
        val cancelIntent = Intent(context, AlarmService::class.java)
        cancelIntent.putExtra("cancelFlag", true)
        cancelIntent.action = AlarmService.STOP_FOREGROUND
        val pendingCancelIntent =
            PendingIntent.getService(context, 0, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(context, SubwayAlarmApp.ALARM_CHANNEL_ID)
            .setContentTitle("지하철 알람 앱")
            .setSmallIcon(R.mipmap.ic_subway_alarm_round)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(
                NotificationCompat.Action(
                    R.mipmap.ic_subway_alarm_round,
                    "Stop",
                    pendingCancelIntent
                )
            )
    }
}