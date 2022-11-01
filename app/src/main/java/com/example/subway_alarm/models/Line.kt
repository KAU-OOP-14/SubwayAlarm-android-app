package com.example.subway_alarm.models

import java.io.File

open class Line{
    var lineId: Int = 0
    val stations: MutableList<Station>
    val endPointList: MutableList<String>

    init {
        stations = mutableListOf()
        endPointList = mutableListOf()
    }

    constructor(){
        lineId = 0
    }

    constructor(id: Int) {
        lineId = lineId
    }


    fun addStations(newStation: Station) {
        stations.add(newStation)
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