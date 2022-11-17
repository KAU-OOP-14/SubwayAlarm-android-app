package com.example.subway_alarm.ui.activities

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var stationId: Int = 0

    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel by viewModel<ViewModelImpl>()
    lateinit var binding: ActivityMainBinding
    var lastTimeBackPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수

    /*
    /** fragment를 열어주는 함수, 추후 리펙토링 예정 */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frgMain.id, fragment)
            commit()
        }
    }
    */

    //Listener역할을 할 Interface 생성
    interface onBackPressedListener{
        fun onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* view와 activity binding */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                println("Touch down evnet ${event.rawX}, ${event.rawY}")
            }
        }
        return super.onTouchEvent(event)
    }


}