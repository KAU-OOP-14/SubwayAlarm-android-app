package com.example.subway_alarm.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.subway_alarm.data.api.dataModel.ApiModelList
import com.example.subway_alarm.data.api.service.NetworkService
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.viewModel.ViewModelImpl
import com.example.subway_alarm.ui.fragments.MainFragment
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    /*
    view model DI(의존성 주입)
    view는 모든 로직 처리를 view model에게 접근해서 합니다.
     */
    val viewModel: ViewModelImpl by viewModel()
    lateinit var binding: ActivityMainBinding

    /************************* 여기부터 옮겨야 하는 코드입니다.************************/
    //retrofit 객체 생성 / 한번만 실행하면 됩니다.
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://swopenapi.seoul.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    var networkService: NetworkService = retrofit.create(NetworkService::class.java)
    /************************* 여기까지 옮겨야 하는 코드입니다.************************/


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
        viewModel.data.observe(this, Observer {
            println("main activity에서 view model의 data 변경 : $it")
            var text: String = ""
            for (element in it) {
                text += "${element}\n"
            }
            binding.txtTest.text = text
        })


        /* 이런식으로 viewModel을 통해 input값을 알려줍니다
         모든 데이터 처리는 viewModel이 합니다 */
        binding.hwajeon.setOnClickListener {
            //입력한 역의 api 요청
            //viewModel.requestApiData("홍대입구")
            replaceMainFragment(MainFragment.newInstance("1", "2"))


            /************************* 여기부터 옮겨야 하는 코드입니다.************************/
            //여기에 역 이름을 전달하면 ApiModelList라는 객체를 생성해서 modelList에 전달해줍니다.
            val userListCall = networkService.doGetUserList("홍대입구")
            userListCall.enqueue(object: Callback<ApiModelList> {
                override fun onResponse(call: Call<ApiModelList>,
                response: Response<ApiModelList>) {
                    if(response.isSuccessful)
                        println("통신 성공")
                    val data = response.body()
                    println(data)
                }

                override fun onFailure(call: Call<ApiModelList>, t: Throwable) {
                    println(t.message)
                    println("통신 실패")
                    call.cancel()
                }

            })
            /************************* 여기까지 옮겨야 하는 코드입니다.************************/
        }
    }

}