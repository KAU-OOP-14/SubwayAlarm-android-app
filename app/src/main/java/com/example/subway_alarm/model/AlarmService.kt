package com.example.subway_alarm.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.ui.activities.MainActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmService : Service() {
    private var id = 0
    private var isRunning = false
    private var player: MediaPlayer? = null
    private val notificationManager
        get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private var notification: NotificationCompat.Builder? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        println("서비스 시작")
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                "default",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel);
        }

        val pending = getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        notification = NotificationCompat.Builder(this, "default")
            .setContentTitle("지하철 알람 앱")
            .setContentText("00시 00분에 알람이 울립니다.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pending)

        startForeground(1, notification!!.build());
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val state = intent?.getStringExtra("state") ?: "default"
        val time = intent?.getIntExtra("time", 0) ?: 0

        val now = System.currentTimeMillis()
        val alarmTime = Date(now + time * 1000)
        val dateFormat = SimpleDateFormat("h:mm:ss")

        val new = notification?.apply {
            setContentTitle("지하철 알람 앱")
            setContentText("${dateFormat.format(alarmTime)} 에 알람이 울립니다.")
        } ?: NotificationCompat.Builder(this, "default")
            .setContentTitle("지하철 알람 앱")
            .setContentText("알람이 설정되지 않았습니다.")
            .setSmallIcon(R.mipmap.ic_launcher)


        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            alarmCount(time)
        }

            id = when (state) {
                "on" -> 1
                "off" -> 0
                else -> 0
            }

        if (!isRunning && id == 1) {
            notificationManager.notify(1, new.build())
            //음악 재생
            player = MediaPlayer.create(this, R.raw.iphonealarm)
            player?.isLooping = true
            player?.start()
            println("알람음 울림~~")

            isRunning = true
            id = 0
        } else if (isRunning && id == 0) {
            player?.let {
                it.stop()
                it.reset()
                it.release()
            }
            isRunning = false
            id = 0
            stopSelf()
        } else if (!isRunning) {
            isRunning = false
            id = 0
            stopSelf()

        } else {
            isRunning = true
            id = 1
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        println("서비스 종료")
        super.onDestroy()
    }

    /** 입력받은 time만큼 초를 세는 background 함수입니다. */
    suspend fun alarmCount(time: Int) {
        var count = time
        while (count >= 0) {
            delay(1000)
            count--
            println(count)
        }
    }


}