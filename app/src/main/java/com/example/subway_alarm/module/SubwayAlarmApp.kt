package com.example.subway_alarm.module

import android.app.Application
import android.content.Context
import org.koin.core.context.startKoin

class SubwayAlarmApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule, appViewModule)
        }
    }
    init{
        instance = this
    }

    companion object {
        lateinit var instance: SubwayAlarmApp

        /**
         * ApplicationContext가 필요할 때 호출하는 static 함수입니다.
         */
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }
}