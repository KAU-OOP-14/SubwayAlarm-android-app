package com.example.subway_alarm.models

class Station(
    val stationName: String,
    val id: Int,
    ): Line() {
    var isFavorited = false // 즐겨찾기 포함 여부
    var leftStation: Station? = null
    var rightStation: Station? = null
    var left2Station: Station? = null
    var right2Station: Station? = null

    fun getStnName(): String {
        return stationName
    }


}