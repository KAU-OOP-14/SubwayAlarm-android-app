package com.example.subway_alarm.ui.activities

import android.app.*
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.PointF
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.model.AlarmReceiver
import com.example.subway_alarm.viewModel.ArrivalViewModel
import com.example.subway_alarm.viewModel.PositionViewModel
import com.example.subway_alarm.viewModel.listener.OnAlarmOff
import com.example.subway_alarm.viewModel.listener.OnAlarmSet
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), OnAlarmSet, OnAlarmOff {
    private var stationId: Int = 0
    lateinit var binding: ActivityMainBinding

    //private lateinit var mImageView: ImageView

    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel by viewModel<ArrivalViewModel>()
    //lateinit var nManager: NotificationManager
    lateinit var alarmManager: AlarmManager
    lateinit var myIntent: Intent
    lateinit var pendingIntent: PendingIntent
    private val posViewModel by viewModel<PositionViewModel>()
    var lastTimeTouchPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 알람시간이 다 되는지 관찰합니다.
        viewModel.alarmTime.observe(this) {
            /*
            if (it == 1) {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(applicationContext, notification).run {
                    play()
                }
                nManager.cancelAll()
            }
             */
        }

        //알람 서비스 생성
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        //알람 리시버 생성
        myIntent = Intent(this, AlarmReceiver::class.java)



        /* view와 activity binding */
        setContentView(binding.root)
        //mImageView= FragmentEntryBinding?.bind(findViewById(R.id.stationImage))
    }


    // 제스처 이벤트가 발생하면 실행되는 메소드
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 제스처 이벤트를 처리하는 메소드를 호출
        if(event != null){
            val pos = PointF(event.x, event.y)
            println("current x: ${event.x}, y : ${event.y}")
            when (event.action) {
                // 화면에 손가락이 닿음
                MotionEvent.ACTION_DOWN -> {
                    posViewModel.setPos(pos)
                    posViewModel.setMoving(true)
                    lastTimeTouchPressed = System.currentTimeMillis()
                    println("Touched")
                }

                // 화면에 손가락이 닿은 채로 움직이고 있음
                MotionEvent.ACTION_MOVE -> {
                    println("moving")
                    posViewModel.setMovePos(pos)
                }

                // 화면에서 손가락을 땜
                MotionEvent.ACTION_UP -> {
                    println("end")
                    posViewModel.setMoving(false)
                    // 잠깐 터치한 경우는 selectedPos를 업데이트 한다
                    if (System.currentTimeMillis() - lastTimeTouchPressed < 180)
                        posViewModel.setSelectedPos(pos)
                    else {
                        posViewModel.setMovePos(pos)
                    }
                }

                else -> return false
            }
        }
        return true
    }


    /*
    private fun showNotification(Title: String, Body: String) {
        val pending = getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, "id")
        builder.setSmallIcon(R.drawable.train)
            .setContentTitle(Title)
            .setContentText(Body)
            .setContentIntent(pending)
            .setAutoCancel(false)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

        nManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nManager.createNotificationChannel(
                NotificationChannel(
                    "id",
                    "name",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        nManager.notify(0, builder.build())
    }

     */

    override fun onAlarmSet() {
        /*
        val now = System.currentTimeMillis()
        val alarmTime = Date(now + viewModel.alarmTime.value * 1000)
        val dateFormat = SimpleDateFormat("hh:mm:ss")
        showNotification("알람 예약", "${dateFormat.format(alarmTime)} 에 알람이 울립니다")
        println(viewModel.alarmTime.value)
         */
        myIntent.putExtra("state","on")
        myIntent.putExtra("time",viewModel.alarmTime.value)
        pendingIntent =
            PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntent)
    }

    override fun onAlarmOff() {
        alarmManager.cancel(pendingIntent)
        myIntent.putExtra("state","off")
        sendBroadcast(myIntent)
    }



}