package com.example.subway_alarm.model

class Station(
    /** 역 이름 */
    val stationName: String,
    /** 역 id */
    val id: Int,
    /** 호선 정보 리스트 */
    val lineList: MutableList<Int>
) {
    var isFavorited = false // 즐겨찾기 포함 여부
    var leftStation: Station? = null
    var rightStation: Station? = null
    var left2Station: Station? = null
    var right2Station: Station? = null
    var endPoint = arrayOfNulls<String>(2)

    /** 방향에 따른 노드를 전달해줍니다.
     * 노드가 null일 경우를 생각해야 합니다.
     * direction으로 left, right가 올 수 있다.
     * null 예외처리를 잘해줘야 한다!
     */
    fun getNode(direction: String): Array<Station?> {
        val stationArray = arrayOfNulls<Station>(2)
        if(direction == "left"){
            stationArray[0] = leftStation
            if(left2Station != null)
                stationArray[1] = left2Station
        }
        if(direction == "right"){
            stationArray[0] = rightStation
            if(right2Station != null)
                stationArray[1] = right2Station
        }
        return stationArray
    }

    fun getStnName() = stationName

    fun getStnId() = id

    fun getIsFavorited() = isFavorited

}