package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.service.OnStationChanged
import com.example.subway_alarm.model.repository.StationRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent

class ViewModelImpl(
) : ViewModel(), OnStationChanged, KoinComponent{
    enum class Direction { LEFT, RIGHT }

    //view에서 observe 중인 값
    private val _apis = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값", 0, mutableListOf()))
    private val stationRepository = StationRepositoryImpl(this)


    private val disposables = io.reactivex.rxjava3.disposables.CompositeDisposable()

    val apis: NonNullLiveData<List<ApiModel>>
        get() = _apis
    val curStation: NonNullLiveData<Station>
        get() = _curStation

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
    }


    /** repository의 api가 바뀌면 view에서 관찰하는 apis를 변경합니다. */
    private fun getRetrofit(stationName: String) {
        stationRepository.retrofitGetStation(stationName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                // 호선에 맞는 api data 만 집어넣어줍니다.
                try {
                    _apis.value = it.realtimeArrivalList!!
                    println("api를 성공적으로 불러왔습니다.")
                } catch (e: NullPointerException) {
                    // api를 못 받았을시 빈 model list를 반환
                    println("NPE : api를 받지 못했습니다..ㅠㅠ")
                    _apis.value = listOf()
                }
            }, {
                println("api를 받지 못했습니다..ㅠㅠ")
            })
            .addTo(disposables)
    }

    /** repository의 cur station을 새롭게 설정합니다. */
    fun onStationSelect(stationName: String) {
        stationRepository.search(stationName)
        if (stationRepository.searchResultList.isNotEmpty()) {
            println("새로운 curruent station set : ${stationRepository.searchResultList[0].stationName}")
            _curStation.value = stationRepository.searchResultList[0]
            stationRepository.curStation = stationRepository.searchResultList[0]
        } else return
    }

    /** 근접한 station으로 방향을 지정해 이동합니다. */
    fun gotoStation(direction: Enum<Direction>, i: Int = -1) {
        val node1: Station?
        val node2: Station?
        when (direction) {
            Direction.LEFT -> {
                node1 = stationRepository.curStation.leftStation
                node2 = stationRepository.curStation.left2Station
            }
            Direction.RIGHT -> {
                node1 = stationRepository.curStation.rightStation
                node2 = stationRepository.curStation.right2Station
            }
            else -> return
        }
        //새로운 api를 호출합니다.
        if (i == -1) {
            if (node1 != null) {
                stationRepository.curStation = node1
                println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
                _curStation.value = stationRepository.curStation
            } else {
                println("다음 역이 없습니다.")
            }
        } else {
            if (i == 0)
                stationRepository.curStation = node1 ?: return
            else
                stationRepository.curStation = node2 ?: return
            println("새로운 cur Station set : ${stationRepository.curStation.stationName}")
            _curStation.value = stationRepository.curStation
        }
    }

    /**
     * Main Fragment에서 이동할 때 갈림길이 있는지 판단하는 함수
     * null를 반환하면 갈림길이 아니라는 의미이다
     */
    fun onCrossedLine(direction: String): Array<String>? {
        return stationRepository.getCrossedLine(direction)
    }

    /** line을 바꿀 때 호출되는 함수입니다.
     * 리팩토링 필요
     * */
    fun changeLine(lineNum: Int) {
        println("line chaged : $lineNum")
        stationRepository.search(curStation.value.getStnName())
        val list = stationRepository.searchResultList
        for(station in list) {
            if(station.id/100 == lineNum) {
                stationRepository.curStation = station
                _curStation.value = station
                return
            }
        }

    }


    /** Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다. */
    fun setAlarm() {

    }

    fun search(stationName: String) {
        //stationRepository.search(stationName)
        // adapter()
    }

    fun showSearchResult(stationName: String) {

    }

    /** 즐겨찾기 목록에 현재 station을 넣습니다. */
    fun addFavorite() {
        stationRepository.favoritStations.add(stationRepository.curStation)
    }

    /** 즐겨찾기 목록에서 station을 삭제합니다. */
    fun deleteFavorite(stationName: String) {
        for (station in stationRepository.favoritStations) {
            if (station.stationName == stationName) {
                stationRepository.favoritStations.remove(station)
                return
            }
        }
        println("Not deleted!!")
    }

    fun create() {}

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    override fun changeStation(newStation: Station) {
        getRetrofit(newStation.stationName)
    }


}