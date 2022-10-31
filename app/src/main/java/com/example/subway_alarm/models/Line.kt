package com.example.subway_alarm.models

import java.io.File

open class Line(){
    val lineId: Int
    val stations: MutableList<Station>
    val endPointList: MutableList<String>
    init {
        lineId = 1001
        stations = mutableListOf()
        endPointList = mutableListOf()
    }


    fun searchStation(stationName: String): Station? {
        for(station in stations) {
            if(station.getStnName() == stationName) {
                return station
            }
        }

        println("찾는 역이 없습니다")
        return null
    }
}