package com.example.subway_alarm.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.viewModel.ViewModelImpl
import com.example.subway_alarm.ui.fragments.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel: ViewModelImpl by viewModel()
    lateinit var binding: ActivityMainBinding



    /** fragment를 열어주는 함수, 추후 리펙토링 예정 */
    private fun replaceMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frgMain.id, fragment)
            commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /* view와 activity binding */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /* View Model과 View 연결 */
        viewModel.apis.observe(this, Observer {
            println("main activity에서 view model의 data 변경 : $it")
            var text: String = ""
            for(model in it) {
                text += "${model.statnNm}|${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}|${model.arvlMsg3}"
                text += "\n-----------------------"
            }
            binding.txtTest.text = text

        })


        /* 이런식으로 viewModel을 통해 input값을 알려줍니다
         모든 데이터 처리는 viewModel이 합니다 */
        binding.hwajeon.setOnClickListener {
            //입력한 역의 api 요청
            replaceMainFragment(MainFragment.newInstance("1", "2"))
        }

        binding.btnGotoSearch.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
    }

}