package com.example.subway_alarm.model

open class Line(val lineId: Int = 0){
    val stations: MutableList<Station>
    val endPointList: MutableList<String>

    init {
        stations = mutableListOf()
        endPointList = mutableListOf()
    }

    fun addStations(newStation: Station) {
        stations.add(newStation)
    }

    fun searchStationInLine(stationName: String): Station? {
        for(station in stations) {
            if(station.stationName == stationName) {
                return station
            }
        }
        return null
    }

    fun setStations(){
        val size = stations.size
        var i: Int = 0
        for(s in stations){
            // 일반적인 left, right station 초기화
            val index = i
            if(index == 0)
                s.rightStation = stations[1]
            else if (index >= (size-1))
                s.leftStation = stations[size-2]
            else{
                s.leftStation = stations[index-1]
                s.rightStation = stations[index+1]
            }

            if(lineId == 1){ // 1호선
                if(index <= 41){
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[1]
                    if(index == 41)
                        s.right2Station = stations[62] // 가산디지털단지
                }
                else if(index <= 61){
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[2]
                    if(index == 61)
                        s.rightStation = null // 인천(종착)
                }
                else{
                    s.endPoint[0] = endPointList[3]
                    s.endPoint[1] = endPointList[4]
                    if(index == 62) // 가산디지털단지
                        s.leftStation = stations[41] // 구로
                }
            }

            else if(lineId == 2){ // 2호선
                if(index == 10){        // 신도림역
                    s.endPoint[0] = endPointList[2]
                    s.endPoint[1] = endPointList[1]
                    s.left2Station = stations[50] // 도림천 역
                }
                else if(index == 33){   // 성수역
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[5]
                    s.right2Station = stations[43] // 용답 역
                }
                else if(index >= 47){
                    s.endPoint[0] = endPointList[3]
                    s.endPoint[1] = endPointList[4]
                    if(index == 50) // 도림천 역
                        s.rightStation = stations[10] // 신도림역
                    else if(index == 47) // 까치산(종착)
                        s.leftStation = null
                }
                else if(index >= 43){
                    s.endPoint[0] = endPointList[6]
                    s.endPoint[1] = endPointList[7]
                    if(index == 43) // 용답 역
                        s.leftStation = stations[33] // 성수역
                    else if (index == 46) // 신설동(종착)
                        s.rightStation = null
                }
                else{
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[1]
                    if(index == 0)          // 순환 만들어주기
                        s.leftStation = stations[42]
                    else if(index == 42)    // 순환 만들어주기
                        s.rightStation = stations[0]
                }
            }

            else if(lineId == 5){ // 5호선
                s.endPoint[0] = endPointList[0]
                if(index <= 38){
                    s.endPoint[1] = endPointList[1]
                    if(index == 38) // 강동역
                        s.right2Station = stations[46]  // 길동역
                }
                else if(index <= 45){
                    s.endPoint[1] = endPointList[2]
                    if(index == 45) // 마천
                        s.rightStation = null
                }
                else{
                    s.endPoint[1] = endPointList[3]
                    if(index == 46) // 길동
                        s.leftStation = stations[38] // 강동
                }
            }

            else if(lineId == 6){ // 6호선
                if(index <= 4){
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[1]
                    s.leftStation = null // 단선
                }
                else{
                    s.endPoint[0] = endPointList[2]
                    s.endPoint[1] = endPointList[1]
                    if (index == 5) // 응암
                        s.leftStation = stations[0] // 역촌
                }
            }

            else if(lineId == 10){    // 경의 중앙선
                s.endPoint[0] = endPointList[0]
                if(index <= 19){
                    s.endPoint[1] = endPointList[1]
                    if(index == 19) // 가좌역인 경우
                        s.right2Station = stations[53] // 신촌
                }
                else if(index <= 52){
                    s.endPoint[1] = endPointList[2]
                    if(index == 52) // 지평
                        s.rightStation = null
                }
                else{
                    s.endPoint[1] = endPointList[3]
                    if(index == 53) // 신촌
                        s.leftStation = stations[19] // 가좌
                }
            }

            else if(lineId == 12){    // 경춘선
                s.endPoint[0] = endPointList[0]
                s.endPoint[1] = endPointList[1]
                if(index == 0)  // 광운대
                    s.rightStation = stations[4] // 상봉역
                else if(index == 1)  // 청량리(종착)
                    s.leftStation = null
                else if(index == 4) // 상봉역
                    s.left2Station = stations[0] // 광운대
            }

            else{ // 나머지 경우
                s.endPoint[0] = endPointList[0]
                s.endPoint[1] = endPointList[1]
            }
            i++
        }
    }
}