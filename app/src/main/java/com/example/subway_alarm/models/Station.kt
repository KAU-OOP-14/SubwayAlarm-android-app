package com.example.subway_alarm.models

class Station(
    val stationName: String,
    val id: Int,
    val lineList: MutableList<Int>
    ) {
    var isFavorited = false // 즐겨찾기 포함 여부
    var leftStation: Station? = null
    var rightStation: Station? = null
    var left2Station: Station? = null
    var right2Station: Station? = null
    var endPoint = arrayOf("", "")

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

    /*
    // Station 객체의 left,right station 및 endPoint 초기화해주는 함수
    fun set(){
        // 일반적인 left, right station 초기화
        val index = id%100
        val size = super.stations.size

        if(index == 0)
            rightStation = super.stations[1]
        else if (index >= (size-1))
            leftStation = super.stations[size-2]
        else{
            leftStation = super.stations[index-1]
            rightStation = super.stations[index+1]
        }

        if(super.lineId == 1){ // 1호선
            if(index <= 41){
                endPoint[0] = super.endPointList[0]
                endPoint[1] = super.endPointList[1]
                if(index == 41)
                    right2Station = super.stations[62] // 가산디지털단지
            }
            else if(index <= 61){
                endPoint[0] = super.endPointList[0]
                endPoint[1] = super.endPointList[2]
                if(index == 61)
                    rightStation = null // 인천(종착)
            }
            else{
                endPoint[0] = super.endPointList[3]
                endPoint[1] = super.endPointList[4]
            }
        }

        else if(super.lineId == 2){ // 2호선
            if(index == 10){        // 신도림역
                endPoint[0] = super.endPointList[2]
                endPoint[1] = super.endPointList[1]
                left2Station = super.stations[50] // 도림천 역
            }
            else if(index == 33){   // 성수역
                endPoint[0] = super.endPointList[0]
                endPoint[1] = super.endPointList[5]
                right2Station = super.stations[43] // 용답 역
            }
            else if(index >= 47){
                endPoint[0] = super.endPointList[3]
                endPoint[1] = super.endPointList[4]
                if(index == 50) // 도림천 역
                    rightStation = super.stations[10] // 신도림역
                else if(index == 47) // 까치산(종착)
                    leftStation = null
            }
            else if(index >= 43){
                endPoint[0] = super.endPointList[6]
                endPoint[1] = super.endPointList[7]
                if(index == 43) // 용답 역
                    leftStation = super.stations[33] // 성수역
                else if (index == 46) // 신설동(종착)
                    rightStation = null
            }
            else{
                endPoint[0] = super.endPointList[0]
                endPoint[1] = super.endPointList[1]
                if(index == 0)          // 순환 만들어주기
                    leftStation = super.stations[42]
                else if(index == 42)    // 순환 만들어주기
                    rightStation = super.stations[0]
            }
        }

        else if(super.lineId == 5){ // 5호선
            endPoint[0] = super.endPointList[0]
            if(index <= 38){
                endPoint[1] = super.endPointList[1]
                if(index == 38) // 강동역
                    right2Station = super.stations[46]  // 길동역
            }
            else if(index <= 45){
                endPoint[1] = super.endPointList[2]
            }
            else{
                endPoint[1] = super.endPointList[3]
            }
        }

        else if(super.lineId == 6){ // 6호선
            if(index <= 4){
                endPoint[0] = super.endPointList[0]
                endPoint[1] = super.endPointList[1]
                leftStation = null // 단선
            }
            else{
                endPoint[0] = super.endPointList[2]
                endPoint[1] = super.endPointList[1]
                if (index == 5) // 응암
                    leftStation = super.stations[0] // 역촌
            }
        }

        else if(super.lineId == 10){    // 경의 중앙선
            endPoint[0] = super.endPointList[0]
            if(index <= 19){
                endPoint[1] = super.endPointList[1]
                if(index == 19) // 가좌역인 경우
                    right2Station = super.stations[53] // 신촌
            }
            else if(index <= 52){
                endPoint[1] = super.endPointList[2]
            }
            else{
                endPoint[1] = super.endPointList[3]
            }
        }

        else if(super.lineId == 12){    // 경춘선
            endPoint[0] = super.endPointList[0]
            endPoint[1] = super.endPointList[1]
            if(index == 0)  // 광운대
                rightStation = super.stations[4] // 상봉역
            else if(index == 1)  // 청량리(종착)
                leftStation = null
            else if(index == 4) // 상봉역
                left2Station = super.stations[0] // 광운대
        }

        else{ // 나머지 경우
            endPoint[0] = super.endPointList[0]
            endPoint[1] = super.endPointList[1]
        }
    }
    */

}