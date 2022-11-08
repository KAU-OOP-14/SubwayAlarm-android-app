package com.example.subway_alarm.model.repository

import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import com.example.subway_alarm.model.api.service.NetworkManager
import com.example.subway_alarm.model.api.service.OnStationChanged
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlin.properties.Delegates


/**
 * 즐겨찾기나 알람설정을 해 놓은 Station을 저장하는 저장소입니다.
 */
class StationRepositoryImpl(listener: OnStationChanged) : StationRepository {
    /**
     * application의 생명주기 동안 살아있어야 하는 livedata
     */
    private val subway = SubwayBuilder.initSubway()
    private val callback = listener

    init {
        println("Subway 객체 -> 저장소에 저장됨")
    }

    /**
     * 노선도 클릭이나, 검색, 즐겨찾기를 통해 불러온 current station 입니다.
     */
    val station: Station = Station("초기값",0, mutableListOf())

    override var curStation: Station by Delegates.observable(Station("초기값",0,mutableListOf())) { _, _, new ->
        callback.changeStation(new)
    }

    override var searchResultList: MutableList<Station> = mutableListOf()
    override var favoritStations: MutableList<Station> = mutableListOf()

    /** 역 검색 결과를 searchResultList에 저장합니다. */
    override fun search(stationName: String){
        searchResultList.clear()     // serachResultList 초기화
        if(stationName == "양평"){
            searchResultList.add(Subway.lines[4].stations[12])
            searchResultList.add(Subway.lines[9].stations[49])
            return
        }
        for(line in Subway.lines){
            val tempStation: Station? = line.searchStationInLine(stationName)
            if(tempStation != null){
                searchResultList.add(tempStation)
            }
        }
        return
    }



    override fun retrofitGetStation(stationName: String): Single<ApiModelList> {
        return NetworkManager.subwayApi.getStationArrivals(stationName)
            .map {
                it.let {
                    val checkedList: MutableList<ApiModel> = mutableListOf()
                    val apiModelList = it.realtimeArrivalList
                    if (apiModelList != null) {
                        for (model in apiModelList) {
                            println("호선 : ${model.subwayId}")
                            //1호선부터 9호선
                            when (val id = model.subwayId) {
                                in 1001..1009 -> {
                                    if (curStation.id / 100 == id % 10) {
                                        checkedList.add(model)
                                    }
                                }
                                1063 -> {
                                    if (curStation.id / 100 == 10) {
                                        checkedList.add(model)
                                    }
                                }
                                1065 -> {
                                    if (curStation.id / 100== 11) {
                                        checkedList.add(model)
                                    }
                                }
                                1067 -> {
                                    if (curStation.id / 100 == 12) {
                                        checkedList.add(model)
                                    }
                                }
                                1075 -> {
                                    if (curStation.id / 100 == 13) {
                                        checkedList.add(model)
                                    }
                                }
                                1077 -> {
                                    if (curStation.id / 100 == 14) {
                                        checkedList.add(model)
                                    }
                                }
                                1091 -> {
                                    if (curStation.id / 100 == 15) {
                                        checkedList.add(model)
                                    }
                                }
                                1092 -> {
                                    if (curStation.id / 100 == 16) {
                                        checkedList.add(model)
                                    }
                                }
                            }

                            //다른 호선(경의중앙선:10 / 공항철도:11 / 경춘선:12 / 수인분당선:13 / 신분당선:14 / 자기부상:15 / 우이신설:16)
                        }
                        val list = ApiModelList(null, checkedList.toList())
                        list

                    }
                    else {
                        val list = ApiModelList(null, null)
                        list
                    }
                }
            }
    }

    fun getCrossedLine(direction: String): Array<String>? {
        val stationList: Array<String> = Array(2) { "" }
        if (direction == "right") {
            if (curStation.right2Station != null) {
                stationList[0] = (curStation.rightStation!!.stationName)
                stationList[1] = (curStation.right2Station!!.stationName)
                return stationList
            }
        } else if (direction == "left") {
            if (curStation.left2Station != null) {
                stationList[0] = (curStation.leftStation!!.stationName)
                stationList[1] = (curStation.left2Station!!.stationName)
                return stationList
            }
        }
        return null
    }





}