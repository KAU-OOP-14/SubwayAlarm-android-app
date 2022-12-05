package com.example.subway_alarm.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SubwayAlarmApp: Application() {
    init{
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SubwayAlarmApp)
            modules(appModule, appViewModule)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).run {
                val alarmChannel = NotificationChannel(
                    ALARM_CHANNEL_ID,
                    "Alert Tests",
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannel(alarmChannel)
            }
        }
    }


    companion object {
        lateinit var instance: SubwayAlarmApp
        const val ALARM_CHANNEL_ID = "com.example.subway_alarm.myalarmnotification"

        /**
         * ApplicationContext가 필요할 때 호출하는 static 함수입니다.
         */
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}