package com.example.subway_alarm.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.data.db.ExcelThread
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.db.SubwayBuilder
import com.example.subway_alarm.models.Subway


/** 즐겨찾기나 알람설정을 해 놓은 Station을 저장하는 저장소입니다.
 * 지속적으로 api 데이터를 갱신받고 싶은 Station을 저장합니다.
 */
class StationRepositoryImpl(
    var stationApi: StationApi
): StationRepository {
    val subway: MutableLiveData<Subway> = MutableLiveData<Subway>()
    init {
        println("station repository 생성!")
        subway.value = SubwayBuilder.initSubway()
        println("Subway 객체 생성")
    }

    override fun refreshStations() {
    }

    override fun addStationArrival(stationName: String) {
        stationApi.getApiData()
    }

    override fun updateThisStation(stationName: String) {


    }

}