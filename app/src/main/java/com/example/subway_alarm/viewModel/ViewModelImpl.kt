package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.api.StationApi
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import com.example.subway_alarm.model.api.service.ApiService
import com.example.subway_alarm.model.api.service.NetworkService
import com.example.subway_alarm.model.repository.StationRepository
import org.koin.core.component.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewModelImpl(
    private val stationRepository: StationRepository,
    private val stationApiStorage: StationApi,
    private val apiService: ApiService
) : ViewModel(), KoinComponent {
    private val _data = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    val retrofit: Retrofit
    var networkService: NetworkService

    // Getter
    val data: NonNullLiveData<List<ApiModel>>
        get() = _data

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
        //retrofit 객체 생성 / 한번만 실행하면 됩니다.
        retrofit = Retrofit.Builder()
            .baseUrl("http://swopenapi.seoul.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkService = retrofit.create(NetworkService::class.java)
    }

    fun getService(stationName: String) {
        //여기에 역 이름을 전달하면 ApiModelList라는 객체를 생성해서 modelList에 전달해줍니다.
        val apiCallBack = networkService.doGetUserList(stationName)
        apiCallBack.enqueue(object : Callback<ApiModelList> {
            override fun onResponse(
                call: Call<ApiModelList>,
                response: Response<ApiModelList>
            ) {
                if (response.isSuccessful)
                    println("통신 성공")
                val data = response.body()
                if (data != null) {
                    val model = data.realtimeArrivalList
                    _data.value = model
                }
            }

            override fun onFailure(call: Call<ApiModelList>, t: Throwable) {
                println(t.message)
                println("통신 실패")
                call.cancel()
            }
        })
    }
    /**
     * 스레드가 끝난 후, LiveData를 업데이트 합니다.
     * ApiThread에서 자동 호출 됩니다.
     */
    /*
    fun updateData() {
        val testArr = apiService.getApiModelList().realtimeArrivalList
        _data.value = testArr
    }

    /**
     * 새로운 api를 요청하고, ApiModel을 생성받아 전달받습니다.
     *
     */
    fun requestApiData(stationName: String) {
        apiService.requestApi(stationName)
    }

     */

    /**
     * 사용자가 입력한 값으로 station을 검색하고, 걸과를 반영합니다.
     */
    fun searchStationWithInput(stationName: String) {

    }

    /**
     *Main Fragment에서 오른쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goRight() {
        //현재 station의 right node를 가져옵니다.

    }

    /**
     * Main Fragement에서 왼쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goLeft() {
        //현재 station의 left node를 가져옵니다.

        //새로운 api를 호출합니다.

        //live data를 갱신합니다.

    }

    /**
     * Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun setAlarm() {

    }

/*
    fun getStationData(direction: String) {
        val curStation = stationRepository.getCurrentStation()
        if(curStation != null) {
            //val nextStation: Station? = curStation.getNode(direction)
        }


    }

 */
    override fun onCleared() {
        super.onCleared()
        println("리소스 정리")
    }

}