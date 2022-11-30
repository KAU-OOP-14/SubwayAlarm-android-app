package com.example.subway_alarm.model

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.di.SubwayAlarmApp
import com.example.subway_alarm.ui.activities.MainActivity
import kotlinx.coroutines.*

class AlarmService: Service() {
    lateinit var job : Job

    companion object {
        const val SERVICE_ID = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("새로운 intent : $intent / $this")
        val message = intent?.getStringExtra("message")?:""
        val time = intent?.getIntExtra("time", 0)?:0
        val cancelFlag = intent?.getBooleanExtra("cancelFlag", false)?:false


        val contentIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE)


        //알람 해제를 위한 intent
        val cancelIntent = Intent(this, MainActivity::class.java)
        cancelIntent.putExtra("cancelFlag", true)
        val pendingCancelIntent = PendingIntent.getActivity(this, 0 ,cancelIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, SubwayAlarmApp.ALARM_CHANNEL_ID)
            .setContentTitle("지하철 알람 앱")
            .setContentText("$message 에 알람이 울립니다.")
            .setSmallIcon(R.mipmap.ic_subway_alarm_round)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_subway_alarm_round, "알람 해제", pendingCancelIntent)

        startForeground(SERVICE_ID, notificationBuilder.build())


        job = GlobalScope.launch {
            val notiManager = getSystemService(NotificationManager::class.java)
            for( progress in 1 .. time ) {
                delay(1000)
                notiManager.notify(SERVICE_ID, notificationBuilder.build())
                Log.d("Service", "Progress = $progress")
            }
            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        }
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        println("service 종료 : $this")
        if(job.isActive)
            job.cancel()
    }
}