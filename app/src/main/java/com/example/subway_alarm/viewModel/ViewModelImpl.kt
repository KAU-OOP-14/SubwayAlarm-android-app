package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.service.NetworkManager
import com.example.subway_alarm.model.repository.StationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class ViewModelImpl(
    private val stationRepository: StationRepository,
) : ViewModel() {
    enum class Direction { LEFT, RIGHT }

    //api model list
    private val _apis = NonNullMutableLiveData<List<ApiModel>>(listOf(ApiModel()))
    private val _curStation = NonNullMutableLiveData<Station>(Station("초기값", 0, mutableListOf()))
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
            .subscribe({ it ->
                // 호선에 맞는 api data 만 집어넣어줍니다.
                try {
                    _apis.value = checkLine(it.realtimeArrivalList!!)
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

    fun setStation(stationName: String) {
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
                getService(stationRepository.curStation.stationName)
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
            getService(stationRepository.curStation.stationName)
        }

    }

    /**
     * Main Fragment에서 이동할 때 갈림길이 있는지 판단하는 함수
     * null를 반환하면 갈림길이 아니라는 의미이다
     */
    fun isCrossedLine(direction: String): Array<String>? {
        val stationList: Array<String> = Array(2) { "" }
        if (direction == "right") {
            if (stationRepository.curStation.right2Station != null) {
                stationList[0] = (stationRepository.curStation.rightStation!!.stationName)
                stationList[1] = (stationRepository.curStation.right2Station!!.stationName)
                return stationList
            }
        } else if (direction == "left") {
            if (stationRepository.curStation.left2Station != null) {
                stationList[0] = (stationRepository.curStation.leftStation!!.stationName)
                stationList[1] = (stationRepository.curStation.left2Station!!.stationName)
                return stationList
            }
        }
        return null
    }

    /** api data가 호선에 맞는지 확인하고 넣어줍니다. */
    fun checkLine(list: List<ApiModel>): List<ApiModel> {
        val checkedList: MutableList<ApiModel> = mutableListOf()
        for (model in list) {
            println("호선 : ${model.subwayId}")
            //1호선부터 9호선
            when (val id = model.subwayId) {
                in 1001..1009 -> {
                    if (curStation.value.id == id % 10) {
                        checkedList.add(model)
                    }
                }
                1063 -> {
                    if (curStation.value.id == 10) {
                        checkedList.add(model)
                    }
                }
                1065 -> {
                    if (curStation.value.id == 11) {
                        checkedList.add(model)
                    }
                }
                1067 -> {
                    if (curStation.value.id == 12) {
                        checkedList.add(model)
                    }
                }
                1075 -> {
                    if (curStation.value.id == 13) {
                        checkedList.add(model)
                    }
                }
                1077 -> {
                    if (curStation.value.id == 14) {
                        checkedList.add(model)
                    }
                }
                1091 -> {
                    if (curStation.value.id == 15) {
                        checkedList.add(model)
                    }
                }
                1092 -> {
                    if (curStation.value.id == 16) {
                        checkedList.add(model)
                    }
                }
            }

            //다른 호선(경의중앙선:10 / 공항철도:11 / 경춘선:12 / 수인분당선:13 / 신분당선:14 / 자기부상:15 / 우이신설:16)
        }
        return checkedList.toList()
    }


    /** Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다. */
    fun setAlarm() {

    }

    fun search(stationName: String) {
        //stationRepository.search(stationName)
        // adapter()
    }

    fun showSearchResult(stationName: String) {
        //getService(stationRepository.curStation!!.stationName)

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


}