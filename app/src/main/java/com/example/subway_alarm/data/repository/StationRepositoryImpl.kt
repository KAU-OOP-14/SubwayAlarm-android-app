package com.example.subway_alarm.data.repository

import com.example.subway_alarm.data.api.StationApi


class StationRepositoryImpl(
    var stationApi: StationApi
): StationRepository {

    override fun refreshStations() {
    }

    override fun getStationArrivals(stationName: String): String {
        return stationApi.getStationApiData(stationName)
    }

    override fun updateStations() {
    }

}