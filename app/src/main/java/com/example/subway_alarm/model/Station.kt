package com.example.subway_alarm.model

data class Station(
    /** 역 이름 */
    val stationName: String,
    /** 역 id */
    val id: Int,
    /** 호선 정보 리스트 */
    val lineList: ArrayList<Int>
) {
    var isFavorited = false // 즐겨찾기 포함 여부
    var leftStation: Station? = null
    var rightStation: Station? = null
    var secondLeftStation: Station? = null
    var secondRightStation: Station? = null
    var endPoint = arrayOfNulls<String>(2)
}