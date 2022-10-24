package com.example.subway_alarm.data.repository

import com.example.subway_alarm.data.api.StationApi


class StationRepositoryImpl(
    var stationApi: StationApi
): StationRepository {

    override fun refreshStations() {

    }

    override fun getStationArrivals(): String {
        return stationApi.getStationApiData()
    }

    override fun updateStations() {
    }

}