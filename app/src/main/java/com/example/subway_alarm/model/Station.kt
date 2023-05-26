package com.example.subway_alarm.model

data class Station(
    /** 역 이름 */
    val stationName: String,
    /** 역 id */
    val id: Int,
    /** 호선 정보 리스트 */
    val lineList: ArrayList<Int>
) {
    var isFavorited = false                     // 즐겨찾기 포함 여부
    var leftStation: Station? = null            // 왼쪽 역
    var rightStation: Station? = null           // 오른쪽 역
    var secondLeftStation: Station? = null      // 왼쪽 갈림역
    var secondRightStation: Station? = null     // 오른쪽 갈림역
    var endPoint = arrayOfNulls<String>(2) // 종착역 정보를 가지는 배열
}