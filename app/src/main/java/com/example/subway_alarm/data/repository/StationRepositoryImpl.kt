package com.example.subway_alarm.data.repository

import com.example.subway_alarm.data.adapter.SubwayAdapter
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.models.Subway


/** 즐겨찾기나 알람설정을 해 놓은 Station을 저장하는 저장소입니다.
 * 지속적으로 api 데이터를 갱신받고 싶은 Station을 저장합니다.
 */
class StationRepositoryImpl(
    var stationApi: StationApi
): StationRepository {
    val stations = arrayOfNulls<String>(100) // 이후에 Station class로 변경 예정
    val subway: Subway = SubwayAdapter.createStations()

    override fun refreshStations() {
    }

    override fun addStationArrival(stationName: String) {
        stationApi.getApiData()

    }

    override fun updateThisStation(stationName: String) {


    }

}