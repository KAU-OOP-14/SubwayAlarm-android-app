package com.example.subway_alarm.ui.activities

import android.app.*
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.PointF
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.model.AlarmReceiver
import com.example.subway_alarm.viewModel.AlarmViewModel
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
    private val posViewModel by viewModel<PositionViewModel>()
    private val alarmViewModel by viewModel<AlarmViewModel>()

    // 알람 매니저 관련 변수
    lateinit var myIntent: Intent
    lateinit var pendingIntent: PendingIntent
    lateinit var alarmManager: AlarmManager
    var lastTimeTouchPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        myIntent = Intent(this, AlarmReceiver::class.java)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        pendingIntent = PendingIntent.getBroadcast(
            this, AlarmReceiver.NOTIFICATION_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

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
                    if (System.currentTimeMillis() - lastTimeTouchPressed < 250)
                        posViewModel.setSelectedPos(pos)
                    else {
                        println("move end")
                        posViewModel.setMovePos(pos)
                    }
                }

                else -> return false
            }
        }
        return true
    }


    override fun onAlarmSet() {
        //알람 처리를 해주는 알람 매니저입니다.
        // 메소드를 actiivity에서만 지원해서 콜백 구조로 구현했습니다.
        val time = (SystemClock.elapsedRealtime() + viewModel.alarmTime.value * 1000)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pendingIntent)
        println("알람이 설정되었습니다.")
    }

    override fun onAlarmOff() {
        alarmManager.cancel(pendingIntent)
        println("알람이 해제되었습니다.")

    }



}