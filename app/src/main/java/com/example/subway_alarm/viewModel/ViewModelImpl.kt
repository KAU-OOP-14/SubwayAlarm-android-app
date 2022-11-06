package com.example.subway_alarm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import com.example.subway_alarm.model.api.service.ApiService
import com.example.subway_alarm.model.api.service.NetworkService
import com.example.subway_alarm.model.repository.StationRepository
import com.example.subway_alarm.model.repository.StationRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewModelImpl(
    private val stationRepository: StationRepository,
    private val apiService: ApiService
) : ViewModel() {
    //api model list
    private val _apis = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값",0, mutableListOf()))

    // Getter
    val apis: NonNullLiveData<List<ApiModel>>
        get() = _apis
    val curStation: NonNullLiveData<Station>
        get() = _curStation

    //retrofit 관련
    private val retrofit: Retrofit
    private val networkService: NetworkService

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
        //retrofit 객체 생성, 한번만 실행하면 됩니다.
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
                    _apis.value = model
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
     * 새로운 api를 요청하고, ApiModel을 생성받아 전달받습니다.
     *
     */
    fun requestApiData(stationName: String) {
        apiService.requestApi(stationName)
    }

    fun setStation(stationName: String) {
        stationRepository.search(stationName)
        if(stationRepository.searchResultList.isNotEmpty()){
            println("새로운 curruent station set : ${stationRepository.searchResultList[0].stationName}")
            _curStation.value = stationRepository.searchResultList[0]
            stationRepository.curStation = stationRepository.searchResultList[0]
        }
        else return

        /*stationRepository.curStation = Subway.let {
            val list = it.searchStations(stationName)
            if (list != null) {
                println("새로운 curruent station set : ${list[0].stationName}")
                _curStation.value = list[0]
                list[0]
            } else return
        }*/
    }

    /**
     *Main Fragment에서 오른쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goRight(i: Int = -1) {
        //현재 station의 right node를 가져옵니다.
        /**
         * node가 두 개 일때, 새로운 fragment를 띄울 수 있게 구현해야함
         */
        val right = stationRepository.curStation.rightStation
        val right2 = stationRepository.curStation.right2Station

        //새로운 api를 호출합니다.
        /*if (right != null) {
            stationRepository.curStation = right
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
            getService(stationRepository.curStation.stationName)
        }*/
        if(i == -1){
            if(right != null){
                stationRepository.curStation = right
                println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
                _curStation.value = stationRepository.curStation
                getService(stationRepository.curStation.stationName)
            }
            else{
                println("다음 역이 없습니다.")
            }
        }
        else {
            if(i == 0)
                stationRepository.curStation = right ?: return
            else
                stationRepository.curStation = right2 ?: return
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
            getService(stationRepository.curStation.stationName)
        }
    }

    /**
     * Main Fragement에서 왼쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goLeft(i: Int = -1) {
        //현재 station의 left node를 가져옵니다.
        val left = stationRepository.curStation.leftStation
        val left2 = stationRepository.curStation.left2Station

        //새로운 api를 호출합니다.
        if(i == -1){
            if(left != null){
                stationRepository.curStation = left
                println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
                _curStation.value = stationRepository.curStation
                getService(stationRepository.curStation.stationName)
            }
            else{
                println("다음 역이 없습니다.")
            }
        }
        else {
            if(i == 0)
                stationRepository.curStation = left ?: return
            else
                stationRepository.curStation = left2 ?: return
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
            getService(stationRepository.curStation.stationName)
        }

    }

    /**
     * Main Fragment에서 이동할 때 갈림길이 있는지 판단하는 함수
     * null를 반환하면 갈림길이 아니라는 의미이다
     */
    fun isCrossedLine(direction: String): Array<String>?{
        val stationList:Array<String> = Array(2) {""}
        if(direction == "right") {
            if (stationRepository.curStation.right2Station != null) {
                stationList[0] = (stationRepository.curStation.rightStation!!.stationName)
                stationList[1] = (stationRepository.curStation.right2Station!!.stationName)
                return stationList
            }
        }
        else if(direction == "left") {
            if (stationRepository.curStation.left2Station != null) {
                stationList[0] = (stationRepository.curStation.leftStation!!.stationName)
                stationList[1] = (stationRepository.curStation.left2Station!!.stationName)
                return stationList
            }
        }
        return null
    }

    /**
     * Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun setAlarm() {

    }

    fun search(stationName: String) {
        //stationRepository.search(stationName)
        // adapter()
    }

    fun showSearchResult(stationName: String) {
        //getService(stationRepository.curStation!!.stationName)

    }

    fun addFavorite() {
        stationRepository.favoritStations.add(stationRepository.curStation)
    }

    fun deleteFavorite(stationName: String){
        for(station in stationRepository.favoritStations){
            if(station.stationName == stationName){
                stationRepository.favoritStations.remove(station)
                return
            }
        }
        println("Not deleted!!")
    }

    fun create(){

    }


}