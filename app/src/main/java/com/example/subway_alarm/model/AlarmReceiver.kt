package com.example.subway_alarm.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val getString = intent?.getStringExtra("state")?:"default"
        val time = intent?.getIntExtra("time", 0)

        //서비스 intent 생성
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("state", getString)
        serviceIntent.putExtra("time", time)

        //버전이 너무 클 경우 대비
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context?.startForegroundService(serviceIntent);
        }else{
            context?.startService(serviceIntent);
        }
    }
}