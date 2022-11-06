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
    enum class direction { LEFT, RIGHT }

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
                println("success")
                _apis.value = it.realtimeArrivalList
            }, {
                println("failed")
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
     * 근접한 station으로 방향을 지정해 이동합니다.
     */
    fun gotoStation(direction: Enum<direction>, i: Int = -1) {
        val node1: Station?
        val node2: Station?
        when (direction) {
            ViewModelImpl.direction.LEFT -> {
                node1 = stationRepository.curStation.leftStation
                node2 = stationRepository.curStation.left2Station
            }
            ViewModelImpl.direction.RIGHT -> {
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