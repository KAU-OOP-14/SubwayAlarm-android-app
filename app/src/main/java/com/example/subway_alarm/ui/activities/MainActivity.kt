package com.example.subway_alarm.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.service.AlarmService
import com.example.subway_alarm.viewModel.AlarmViewModel
import com.example.subway_alarm.viewModel.ArrivalViewModel
import com.example.subway_alarm.viewModel.BookmarkViewModel
import com.example.subway_alarm.viewModel.PositionViewModel
import com.example.subway_alarm.viewModel.listener.OnAlarmOff
import com.example.subway_alarm.viewModel.listener.OnAlarmSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), OnAlarmSet, OnAlarmOff {
    private var stationId: Int = 0
    lateinit var binding: ActivityMainBinding


    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel by viewModel<ArrivalViewModel>()
    private val positionViewModel by viewModel<PositionViewModel>()
    private val alarmViewModel by viewModel<AlarmViewModel>()
    private val bookmarkViewModel by viewModel<BookmarkViewModel>()


    private val alarmReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val isAlarmStop = intent.getBooleanExtra("alarmStop", false)
            if(isAlarmStop) {
                alarmViewModel.resetAlarm(0)
            }
        }
    }

    // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수
    var lastTimeTouchPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //alarm Service와 broadcast 데이터 송수신
        LocalBroadcastManager.getInstance(this).registerReceiver(alarmReceiver, IntentFilter("alarm"))


        CoroutineScope(Dispatchers.Main).launch {
            bookmarkViewModel.getFavorites()
        }

        // dispaly의 픽셀 수 구하기
        // height는 상단의 상태 바와 하단의 navigationBar 크기를 제외한 픽셀 수가 나온다.
        val display = this.applicationContext.resources.displayMetrics
        //println("widthPiexels : ${display.widthPixels}, heightPixels : ${display.heightPixels}")

        var statusBarHeight = 0
        var resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        //println("status : $statusBarHeight")

        resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        var navigationBarHeight = 0
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        //println("devie: $navigationBarHeight")
        positionViewModel.initPixels(display.widthPixels, display.heightPixels, statusBarHeight, navigationBarHeight)

        /* view와 activity binding */
        setContentView(binding.root)
    }


    // 제스처 이벤트가 발생하면 실행되는 메소드
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 제스처 이벤트를 처리하는 메소드를 호출
        if(event != null){
            val pos = PointF(event.x, event.y)
            //println("current x: ${event.x}, y : ${event.y}")
            when (event.action) {
                // 화면에 손가락이 닿음
                MotionEvent.ACTION_DOWN -> {
                    positionViewModel.setPos(pos)
                    positionViewModel.setMoving(true)
                    lastTimeTouchPressed = System.currentTimeMillis()
                    //println("Touched")
                }

                // 화면에 손가락이 닿은 채로 움직이고 있음
                MotionEvent.ACTION_MOVE -> {
                    //println("moving")
                    positionViewModel.setMovePos(pos)
                }

                // 화면에서 손가락을 땜
                MotionEvent.ACTION_UP -> {
                    //println("end")
                    positionViewModel.setMoving(false)
                    // 잠깐 터치한 경우는 selectedPos를 업데이트 한다
                    if (System.currentTimeMillis() - lastTimeTouchPressed < 250)
                        positionViewModel.setSelectedPos(pos)
                    else {
                        //println("move end")
                        positionViewModel.setMovePos(pos)
                    }
                }

                else -> return false
            }
        }
        return true
    }


    override fun onAlarmSet() {
        //알람 리시버 생성
        val myIntent = Intent(this, AlarmService::class.java)
        myIntent.putExtra("time", alarmViewModel.alarmTime.value)
        myIntent.action = AlarmService.START_FOREGROUND
        //알람 처리를 해주는 알람 매니저입니다.
        // 메소드를 actiivity에서만 지원해서 콜백 구조로 구현했습니다.
        //alarm 매니저 늦은 초기화
        ContextCompat.startForegroundService(this, myIntent)
    }

    override fun onAlarmOff() {
        // 알람 취소
        val myIntent = Intent(this, AlarmService::class.java)
        myIntent.action = AlarmService.STOP_FOREGROUND
        ContextCompat.startForegroundService(this, myIntent)
    }

}