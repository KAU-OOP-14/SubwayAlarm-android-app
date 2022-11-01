package com.example.subway_alarm.data.repository

import com.example.subway_alarm.models.Station

interface StationRepository {
    /** Station Repository에 저장된 모든 station에 대한 api를 업데이트 합니다. */
    fun refreshStations()

    /** 새로운 Station에 대한 api를 받아옵니다. */
    fun addStationArrival(stationName: String)

    /** 현재 Station을 리턴합니다. */
    fun getCurrentStation(): Station?

    /** 현재 Station을 설정합니다. */
    fun setCurrentStation()


}