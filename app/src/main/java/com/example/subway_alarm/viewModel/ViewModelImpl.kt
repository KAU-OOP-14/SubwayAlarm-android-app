package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.repository.StationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class ViewModelImpl(
    private val stationRepository: StationRepository
) : ViewModel() {
    enum class Direction { LEFT, RIGHT }

    //view에서 observe 중인 값
    private val _leftApi = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _rightApi = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값", 0, mutableListOf()))
    private val _searchText = NonNullMutableLiveData<MutableList<Station>>(mutableListOf())


    private val disposables = io.reactivex.rxjava3.disposables.CompositeDisposable()

    val leftApi: NonNullLiveData<List<ApiModel>>
        get() = _leftApi
    val rightApi: NonNullLiveData<List<ApiModel>>
        get() = _rightApi
    val curStation: NonNullLiveData<Station>
        get() = _curStation
    val searchText: NonNullLiveData<MutableList<Station>>
        get() = _searchText

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
    }

    /** repository에 새로운 station을 set해주고 api를 요청합니다. */
    fun newStation(station: Station) {
        stationRepository.curStation = station
        _curStation.value = station
        getRetrofit(station.stationName)
    }

    /** repository의 api가 바뀌면 view에서 관찰하는 apis를 변경합니다. */
    private fun getRetrofit(stationName: String) {
        stationRepository.retrofitGetArrivals(stationName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
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
            }, {
                println("api를 받지 못했습니다..ㅠㅠ")
            })
            .addTo(disposables)
    }

    /** station 이름을 전달받아 검색한 결과로 repository의 cur station을 새롭게 설정합니다. */
    fun onStationSelect(stationId: Int) {
        newStation(Subway.searchWithId(stationId))
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
                newStation(node1)
            } else {
                println("다음 역이 없습니다.")
            }
        } else {
            if (i == 0)
                node1?.let {
                    newStation(node1)
                }
            else
                node2?.let {
                    newStation(node2)
                }
        }
    }

    /**
     * Main Fragment에서 이동할 때 갈림길이 있는지 판단하는 함수.
     * null를 반환하면 갈림길이 아니라는 의미입니다.
     */
    fun onCrossedLine(direction: String): Array<String>? {
        return stationRepository.getCrossedLine(direction)
    }

    /** line을 바꿀 때 호출되는 함수입니다.
     * 리팩토링 필요 : 호선을 바꿀 때마다 검색을 해야하는 것이 번거로움
     * */
    fun changeLine(lineNum: Int) {
        println("line changed : $lineNum")
        stationRepository.search(curStation.value.stationName)
        val list = stationRepository.searchResultList
        for (station in list) {
            if (station.id / 100 == lineNum) {
                newStation(station)
            }
        }
    }


    /** Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다. */
    fun setAlarm() {

    }


    /** Search Activity에서 검색 결과가 바뀔 때, Subway에 있는 list와 비교해서 매칭한 결과를 livedata로 반영합니다. */
    fun onSearchTextChanged(changedString: String): MutableList<Station> {
        _searchText.value = stationRepository.searchStationsWithName(changedString)
        return _searchText.value
    }

    /** 즐겨찾기 목록에 현재 station을 넣습니다. */
    fun addFavorite() {
        stationRepository.favoriteStations.add(stationRepository.curStation)
    }

    /** 즐겨찾기 목록에서 station을 삭제합니다. */
    fun deleteFavorite(stationName: String) {
        for (station in stationRepository.favoriteStations) {
            if (station.stationName == stationName) {
                stationRepository.favoriteStations.remove(station)
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

}