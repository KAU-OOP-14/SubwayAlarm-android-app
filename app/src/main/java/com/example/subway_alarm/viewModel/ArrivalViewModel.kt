package com.example.subway_alarm.viewModel

import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.repository.StationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*

class ArrivalViewModel(
    private val stationRepository: StationRepository
    ): BaseViewModel() {
    enum class Direction { LEFT, RIGHT }

    private val _leftApi = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _rightApi = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값", 0, arrayListOf()))

    //왼쪽 방향 api
    val leftApi: NonNullLiveData<List<ApiModel>>
        get() = _leftApi
    //오른쪽 방향 api
    val rightApi: NonNullLiveData<List<ApiModel>>
        get() = _rightApi
    val curStation: NonNullLiveData<Station>
        get() = _curStation

    var isLoaded = false

    /** repository의 api가 바뀌면 view에서 관찰하는 apis를 변경합니다. */
    private fun getRetrofit(stationName: String) {
        addToDisposable(
            stationRepository.retrofitGetArrivals(stationName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ //성공시
                    try {
                        _rightApi.value = it.rightList!!
                        _leftApi.value = it.leftList!!
                        println("api를 성공적으로 불러왔습니다.")
                    } catch (e: NullPointerException) {
                        // api를 못 받았을시 빈 model list를 반환
                        println("NPE : api를 받지 못했습니다..ㅠㅠ")
                        _rightApi.value = listOf()
                        _leftApi.value = listOf()
                    }
                }, { //실패시
                    println("api를 받지 못했습니다..ㅠㅠ")
                })
        )
    }

    /** repository에 새로운 station을 set해주고 api를 요청합니다. */
    private fun newStation(station: Station) {
        stationRepository.curStation = station
        _curStation.value = station
        getRetrofit(station.stationName)
    }

    /** station id를 전달받아 검색한 결과로 repository의 cur station을 새롭게 설정합니다. */
    fun onStationSelect(stationId: Int) {
        Subway.getStation(stationId)?.let {
            newStation(it)
        }
    }

    /** 근접한 station으로 방향을 지정해 이동합니다. */
    fun gotoStation(direction: Enum<Direction>, i: Int = -1) {
        val node1: Station?
        val node2: Station?
        when (direction) {
            Direction.LEFT -> {
                node1 = stationRepository.curStation.leftStation
                node2 = stationRepository.curStation.secondLeftStation
            }
            Direction.RIGHT -> {
                node1 = stationRepository.curStation.rightStation
                node2 = stationRepository.curStation.secondRightStation
            }
            else -> return
        }
        //새로운 api를 호출합니다.
        if (i == -1) { // 종착역인 경우
            node1?.let{
                newStation(node1)
            }
        } else { // 역 노드가 하나인 경우
            if (i == 0)
                node1?.let {
                    newStation(node1)
                }
            else
                node2?.let { // 역 노드가 두 개인 경우
                    newStation(node2)
                }
        }
    }

    /** 갈림길이 있을 경우 선택지 역에 대한 배열을 반환합니다. */
    fun onCrossedLine(direction: String): Array<String>? {
        return stationRepository.getCrossedLine(direction)
    }

    /** line을 바꿀 때 호출되는 함수입니다. */
    fun changeLine(lineNum: Int) {
        val list = stationRepository.search(curStation.value.stationName)
        for (station in list) {
            if (station.id / Subway.STATION_ID_UNIT == lineNum) {
                newStation(station)
            }
        }
    }

    // loading시 repository의 DI를 위한 함수입니다.
    fun onLoading(){}

}