package com.example.subway_alarm.model.repository

import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import com.example.subway_alarm.model.api.service.NetworkService
import io.reactivex.rxjava3.core.Single


/** retrofit 데이터를 처리해주는 저장소입니다.
 * main fragment에서 보고 있는 station을 담고 있습니다. */
class StationRepositoryImpl(
    private val stationApi: NetworkService
) : StationRepository {

    init {
        SubwayBuilder.initSubway()
    }

    /** 노선도 클릭이나, 검색, 즐겨찾기를 통해 불러온 current station 입니다. */
    val station: Station = Station("초기값", 0, arrayListOf())

    override var curStation: Station = station
    override val afterSearchedResultList: MutableList<Station> = mutableListOf()
    override var searchResultList: MutableList<Station> = mutableListOf()
    override var favoriteStations: MutableList<Station> = mutableListOf()

    /** 역 검색 결과를 searchResultList에 저장합니다. */
    override fun searchStationName(stationName: String) = Subway.searchStation(stationName)

    /** api data를 map을 통해 조작해서 viewmodel에게 알려줍니다. */
    override fun retrofitGetArrivals(stationName: String): Single<ApiModelList> {
        return stationApi.getStationArrivals(stationName)
            //map을 통해 데이터를 가공합니다.
            .map {
                it.let {
                    val rightCheckedList: MutableList<ApiModel> = mutableListOf()
                    val leftCheckedList: MutableList<ApiModel> = mutableListOf()
                    val apiModelList = it.leftList
                    if (apiModelList != null) {
                        for (model in apiModelList) {
                            //println("호선 : ${model.subwayId}")
                            //1호선부터 9호선
                            when (val id = model.subwayId) {
                                in 1001..1009 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == id % 10) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                //다른 호선(경의중앙선:10 / 공항철도:11 / 경춘선:12 / 수인분당선:13 / 신분당선:14 / 자기부상:15 / 우이신설:16)
                                1063 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 10) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1065 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 11) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1067 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 12) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1075 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 13) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1077 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 14) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1091 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 15) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                                1092 -> {
                                    if (curStation.id / Subway.STATION_ID_UNIT == 16) {
                                        if(model.updnLine == "하행" ||model.updnLine == "내선")
                                            rightCheckedList.add(model)
                                        else
                                            leftCheckedList.add(model)
                                    }
                                }
                            }
                        }
                        val list = ApiModelList(null, leftCheckedList , rightCheckedList)
                        list

                    } else {
                        val list = ApiModelList(null, null,null)
                        list
                    }
                }
            }
    }

    /** 검색한 String을 포함한 모든 Staion List를 반환하는 함수입니다.*/
    override fun searchStationContainingStr(stringName: String) = Subway.searchStationsWithName(stringName)

    /** 갈림길이 나올 경우 선택지에 대한 배열을 반환합니다. */
    override fun getCrossedLine(direction: String): Array<String>? {
        val stationList: Array<String> = Array(2) { "" }
        if (direction == "right") {
            if (curStation.secondRightStation != null) {
                stationList[0] = (curStation.rightStation?.stationName ?: "")
                stationList[1] = (curStation.secondRightStation?.stationName ?: "")
                return stationList
            }
        } else if (direction == "left") {
            if (curStation.secondLeftStation != null) {
                stationList[0] = (curStation.leftStation?.stationName ?: "")
                stationList[1] = (curStation.secondLeftStation?.stationName ?: "")
                return stationList
            }
        }
        return null
    }
}