package com.example.subway_alarm.data.adapter

import com.example.subway_alarm.models.Subway

object SubwayAdapter {

    fun getXslFile(): String {
        return "data"
    }

    fun createStations(): Subway {
        val data = getXslFile()
        return Subway
    }
}