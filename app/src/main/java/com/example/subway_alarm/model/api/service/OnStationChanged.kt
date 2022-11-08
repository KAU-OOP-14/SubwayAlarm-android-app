package com.example.subway_alarm.model.api.service

import com.example.subway_alarm.model.Station

interface OnStationChanged {
    fun changeStation(newStation: Station)
}