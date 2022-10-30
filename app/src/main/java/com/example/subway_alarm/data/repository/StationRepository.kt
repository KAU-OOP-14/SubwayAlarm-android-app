package com.example.subway_alarm.data.repository

interface StationRepository {
    /** Station Repository에 저장된 모든 station에 대한 api를 업데이트 합니다. */
    fun refreshStations()

    /** 새로운 Station에 대한 api를 받아옵니다. */
    fun addStationArrival(stationName: String)

    /** 특정 Station에 대한 api 데이터를 업데이트 합니다. */
    fun updateThisStation(stationName: String)
}