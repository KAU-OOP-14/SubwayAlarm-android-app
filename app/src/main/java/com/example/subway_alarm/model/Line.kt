package com.example.subway_alarm.model

class Line(val lineId: Int = 0){
    private val _stationList: MutableList<Station> = mutableListOf()
    private var endPointList: ArrayList<String> = arrayListOf()

    val stationList: MutableList<Station>
        get() = _stationList

    /** Line의 stationList에서 staitonName을 가지는 Station 객체를 찾는 함수 */
    fun searchStationInLine(stationName: String): Station?{
        for (station in _stationList)
            if(station.stationName == stationName)
                return station
        return null
    }

    fun getStationInLine(stationId: Int): Station?{
        return try {
            _stationList[stationId % 100]
        }catch (e: IndexOutOfBoundsException) {
            println("index 오류 발생 ㅠㅠ : ${(stationId % 100)} / $e")
            null
        }
    }

    /** Line 객체의 stationList 리스트에 Station 객체를 추가하는 함수 */
    fun initStations(newStation: Station){
        _stationList.add(newStation)
    }

    fun initEndPointList(endPointList: ArrayList<String>){
        this.endPointList = endPointList
    }

    /** Station 객체의 왼쪽, 오른쪽 역과 종착역을 설정하는 함수 */
    fun initNearStations(){
        val size = _stationList.size
        for((index, s) in _stationList.withIndex()){
            // 일반적인 left, right station 초기화
            if(index == 0)
                s.rightStation = _stationList[1]
            else if (index >= (size-1))
                s.leftStation = _stationList[size-2]
            else{
                s.leftStation = _stationList[index-1]
                s.rightStation = _stationList[index+1]
            }

            if(lineId == 1){ // 1호선
                if(index <= 41){
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[1]
                    if(index == 41)
                        s.secondRightStation = _stationList[62] // 가산디지털단지
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
                        s.leftStation = _stationList[41] // 구로
                }
            }

            else if(lineId == 2){ // 2호선
                if(index == 10){        // 신도림역
                    s.endPoint[0] = endPointList[2]
                    s.endPoint[1] = endPointList[1]
                    s.secondLeftStation = _stationList[50] // 도림천 역
                }
                else if(index == 33){   // 성수역
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[5]
                    s.secondRightStation = _stationList[43] // 용답 역
                }
                else if(index >= 47){
                    s.endPoint[0] = endPointList[3]
                    s.endPoint[1] = endPointList[4]
                    if(index == 50) // 도림천 역
                        s.rightStation = _stationList[10] // 신도림역
                    else if(index == 47) // 까치산(종착)
                        s.leftStation = null
                }
                else if(index >= 43){
                    s.endPoint[0] = endPointList[6]
                    s.endPoint[1] = endPointList[7]
                    if(index == 43) // 용답 역
                        s.leftStation = _stationList[33] // 성수역
                    else if (index == 46) // 신설동(종착)
                        s.rightStation = null
                }
                else{
                    s.endPoint[0] = endPointList[0]
                    s.endPoint[1] = endPointList[1]
                    if(index == 0)          // 순환 만들어주기
                        s.leftStation = _stationList[42]
                    else if(index == 42)    // 순환 만들어주기
                        s.rightStation = _stationList[0]
                }
            }

            else if(lineId == 5){ // 5호선
                s.endPoint[0] = endPointList[0]
                if(index <= 38){
                    s.endPoint[1] = endPointList[1]
                    if(index == 38) // 강동역
                        s.secondRightStation = _stationList[46]  // 길동역
                }
                else if(index <= 45){
                    s.endPoint[1] = endPointList[2]
                    if(index == 45) // 마천
                        s.rightStation = null
                }
                else{
                    s.endPoint[1] = endPointList[3]
                    if(index == 46) // 길동
                        s.leftStation = _stationList[38] // 강동
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
                        s.leftStation = _stationList[0] // 역촌
                }
            }

            else if(lineId == 10){    // 경의 중앙선
                s.endPoint[0] = endPointList[0]
                if(index <= 19){
                    s.endPoint[1] = endPointList[1]
                    if(index == 19) // 가좌역인 경우
                        s.secondRightStation = _stationList[53] // 신촌
                }
                else if(index <= 52){
                    s.endPoint[1] = endPointList[2]
                    if(index == 52) // 지평
                        s.rightStation = null
                }
                else{
                    s.endPoint[1] = endPointList[3]
                    if(index == 53) // 신촌
                        s.leftStation = _stationList[19] // 가좌
                }
            }

            else if(lineId == 12){    // 경춘선
                s.endPoint[0] = endPointList[0]
                s.endPoint[1] = endPointList[1]
                if(index == 0)  // 광운대
                    s.rightStation = _stationList[4] // 상봉역
                else if(index == 1)  // 청량리(종착)
                    s.leftStation = null
                else if(index == 4) // 상봉역
                    s.secondLeftStation = _stationList[0] // 광운대
            }

            else{ // 나머지 경우
                s.endPoint[0] = endPointList[0]
                s.endPoint[1] = endPointList[1]
            }
        }
    }
}