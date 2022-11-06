package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.service.NetworkManager
import com.example.subway_alarm.model.api.service.NetworkService
import com.example.subway_alarm.model.repository.StationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ViewModelImpl(
    private val stationRepository: StationRepository,
) : ViewModel() {
    //api model list
    private val _apis = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값",0, mutableListOf()))
    private val disposables = io.reactivex.rxjava3.disposables.CompositeDisposable()

    // Getter
    val apis: NonNullLiveData<List<ApiModel>>
        get() = _apis
    val curStation: NonNullLiveData<Station>
        get() = _curStation

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
    }

    fun getService(stationName: String) {
        NetworkManager.subwayApi
            .doGetUserList(stationName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { it->
                println("success")
                _apis.value = it.realtimeArrivalList
            }, {
                println("failed")
            })
            .addTo(disposables)
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
    fun goRight() {
        //현재 station의 right node를 가져옵니다.
        /**
         * node가 두 개 일때, 새로운 fragment를 띄울 수 있게 구현해야함
         */
        val right = stationRepository.curStation.rightStation

        //새로운 api를 호출합니다.
        if (right != null) {
            stationRepository.curStation = right
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
            getService(stationRepository.curStation.stationName)
        }
        else {
            println("다음 역이 없습니다.")
        }
    }

    /**
     * Main Fragement에서 왼쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goLeft() {
        //현재 station의 left node를 가져옵니다.
        val left = stationRepository.curStation.leftStation

        //새로운 api를 호출합니다.
        if (left != null) {
            stationRepository.curStation = left
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
            getService(stationRepository.curStation.stationName)
        }
        else {
            println("다음 역이 없습니다.")
        }

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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


}