package com.example.subway_alarm.models

open class Line(){
    val lineId: Int
    val stations: MutableList<Station>

    init {
        lineId = 1001
        stations = mutableListOf()
    }

    fun searchStation(stationName: String): Station? {
        var _curStation = stations[0]
        for(station in stations) {
            if(station.getStnName() == stationName) {
                return station
                break
            }
        }

        println("찾는 역이 없습니다")
        return null
    }
}