package com.example.subway_alarm.ui.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.viewModel.PositionViewModel
import com.example.subway_alarm.viewModel.ViewModelImpl
import com.example.subway_alarm.viewModel.listener.OnAlarmSet
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), OnAlarmSet {
    private var stationId: Int = 0
    lateinit var binding: ActivityMainBinding

    //private lateinit var mImageView: ImageView
    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel by viewModel<ViewModelImpl>()
    lateinit var nManager: NotificationManager
    val posViewModel: PositionViewModel by viewModel<PositionViewModel>()
    var lastTimeTouchPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수

    /*
    /** fragment를 열어주는 함수, 추후 리펙토링 예정 */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frgMain.id, fragment)
            commit()
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 알람시간이 다 되는지 관찰합니다.
        viewModel.alarmTime.observe(this) {
            if (it == 1) {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(applicationContext, notification).run {
                    play()
                }
                nManager.cancelAll()
            }
        }

        /* view와 activity binding */
        setContentView(binding.root)
        //mImageView= FragmentEntryBinding?.bind(findViewById(R.id.stationImage))
        /*
        /* 이런식으로 viewModel을 통해 input값을 알려줍니다
         모든 데이터 처리는 viewModel이 합니다 */
        binding.btnStation.setOnClickListener {
            //입력한 역의 api 요청
            val bottomSheet = MainFragment()
            bottomSheet.show(supportFragmentManager,bottomSheet.tag)
            //replaceMainFragment(MainFragment.newInstance())
        }

        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabMain.setOnClickListener{
            toggleFab()
        }

        binding.fabSearch.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            toggleFab()
        }

        binding.fabBookmark.setOnClickListener{
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
            toggleFab()
        }

        binding.fabSetting.setOnClickListener{
            Toast.makeText(this, "setting 버튼 클릭!", Toast.LENGTH_SHORT).show()
            toggleFab()
        }

        intent.extras?.getInt("clickedStationId")?.let {
            val bottomSheet = MainFragment()
            val bundle = Bundle()
            bundle.putInt("stationId", it)
            bottomSheet.arguments = bundle
            bottomSheet.show(supportFragmentManager,bottomSheet.tag)
            //replaceFragment(MainFragment.newInstance(it))
        }
        */

    }


    // 제스처 이벤트가 발생하면 실행되는 메소드
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 제스처 이벤트를 처리하는 메소드를 호출
        println("x: ${event?.x}, y : ${event?.y}")
        when (event?.action) {
            // 화면에 손가락이 닿음
            MotionEvent.ACTION_DOWN -> {
                val pos = PointF(event.x, event.y)
                posViewModel.setPos(pos)
                lastTimeTouchPressed = System.currentTimeMillis()
                println("Touched")
            }

            // 화면에 손가락이 닿은 채로 움직이고 있음
            MotionEvent.ACTION_MOVE -> {
                println("moving")
                val pos = PointF(event.x, event.y)
                posViewModel.setMoving(true)
                posViewModel.setMovePos(pos)
            }

            // 화면에서 손가락을 땜
            MotionEvent.ACTION_UP -> {
                println("end")
                if (System.currentTimeMillis() - lastTimeTouchPressed < 500) {
                    posViewModel.setMoving(false)
                }
                val pos = PointF(event.x, event.y)
                posViewModel.setPos(pos)
            }
            else -> return false
        }
        return true
    }


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

override fun onAlarmSet() {
    val now = System.currentTimeMillis()
    val alarmTime = Date(now + viewModel.alarmTime.value * 1000)
    val dateFormat = SimpleDateFormat("hh:mm:ss")
    showNotification("알람 예약", "${dateFormat.format(alarmTime)} 에 알람이 울립니다")
    println(viewModel.alarmTime.value)
}



}