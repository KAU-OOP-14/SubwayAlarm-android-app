package com.example.subway_alarm.viewModel

import com.example.subway_alarm.model.repository.StationRepository

class BookmarkViewModel(
    private val stationRepository: StationRepository
    ): BaseViewModel() {
    /** 즐겨찾기 목록에 현재 station을 넣습니다. */
    fun addFavorite() {
        stationRepository.favoriteStations.add(stationRepository.curStation)
    }

    /** 즐겨찾기 목록에서 station을 삭제합니다. */
    fun deleteFavorite(stationName: String) {
        for (station in stationRepository.favoriteStations) {
            if (station.stationName == stationName) {
                stationRepository.favoriteStations.remove(station)
                return
            }
        }
        println("Not deleted!!")
    }
}