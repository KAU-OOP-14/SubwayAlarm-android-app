package com.example.subway_alarm.model.repository

import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import com.example.subway_alarm.model.api.service.NetworkManager
import io.reactivex.rxjava3.core.Single


/** retrofit 데이터를 처리해주는 저장소입니다.
 * main fragment에서 보고 있는 station을 담고 있습니다. */
class StationRepositoryImpl : StationRepository {
    private val subway = SubwayBuilder.initSubway()

    init {
        println("Subway 객체 -> 저장소에 저장됨")
    }

    /** 노선도 클릭이나, 검색, 즐겨찾기를 통해 불러온 current station 입니다. */
    val station: Station = Station("초기값", 0, mutableListOf())

    override var curStation: Station = station
        set(value) {
            println("새로운 curStation set : ${value.stationName}")
            field = value
        }

    override var searchResultList: MutableList<Station> = mutableListOf()
    override var favoriteStations: MutableList<Station> = mutableListOf()

    /** 역 검색 결과를 searchResultList에 저장합니다. */
    override fun search(stationName: String) {
        searchResultList.clear()     // serachResultList 초기화
        if (stationName == "양평") {
            searchResultList.add(Subway.lines[4].stations[12])
            searchResultList.add(Subway.lines[9].stations[49])
            return
        }
        for (line in Subway.lines) {
            val tempStation: Station? = line.searchStationInLine(stationName)
            if (tempStation != null) {
                searchResultList.add(tempStation)
            }
        }
        return
    }

    /** api data를 map을 통해 조작해서 viewmodel에게 알려줍니다. */
    override fun retrofitGetArrivals(stationName: String): Single<ApiModelList> {
        return NetworkManager.subwayApi.getStationArrivals(stationName)
            //map을 통해 데이터를 가공합니다.
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
                                //다른 호선(경의중앙선:10 / 공항철도:11 / 경춘선:12 / 수인분당선:13 / 신분당선:14 / 자기부상:15 / 우이신설:16)
                                1063 -> {
                                    if (curStation.id / 100 == 10) {
                                        checkedList.add(model)
                                    }
                                }
                                1065 -> {
                                    if (curStation.id / 100 == 11) {
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
                        }
                        val list = ApiModelList(null, checkedList.toList())
                        list

                    } else {
                        val list = ApiModelList(null, null)
                        list
                    }
                }
            }
    }

    /** 갈림길이 나올 경우 선택지에 대한 배열을 반환합니다. */
    override fun getCrossedLine(direction: String): Array<String>? {
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