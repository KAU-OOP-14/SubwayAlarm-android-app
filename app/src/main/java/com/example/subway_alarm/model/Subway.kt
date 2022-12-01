package com.example.subway_alarm.model

object Subway {
    private val lineList: MutableList<Line> = mutableListOf()

    init {
        println("Subway 생성")
    }

    /** SubwayBuilder에서 Subway 객체에 Line 객체들을 초기화 해주는 함수 */
    fun addLines(lineList: MutableList<Line>){
        for (line in lineList){
            //line.initEndPointList(subwayEndPointList[line.lineId - 1])
            //line.initNearStations()
            this.lineList.add(line)
        }
    }

    // firebase에서 endPointList를 받아와서 초기화 해주는 함수
    fun initLines(subwayEndPointMap: MutableMap<Int, ArrayList<String>>){
        for (line in lineList){
            line.initEndPointList(subwayEndPointMap.getValue(line.lineId))
            line.initNearStations()
        }
    }

    /** stationId를 이용해서 Station 객체를 반환하는 함수
     *  올바르지 않는 stationId(index 오류)이면 null 반환
     */
    fun getStation(stationId: Int): Station?{
        return try {
            lineList[(stationId / 1000) - 1].getStationInLine(stationId)
        }catch (e: IndexOutOfBoundsException) {
            println("index 오류 발생 ㅠㅠ : ${(stationId / 1000)} / $e")
            null
        }
    }

    /** 매개변수 stationName을 이름으로로 가지 역들을 검색하고 반환합니다 */
    fun searchStation(stationName: String): MutableList<Station> {
        val searchResultList: MutableList<Station> = mutableListOf()

        if (stationName == "양평") {
            this.getStation(412)?.let{ station ->
                searchResultList.add(station)
            }
            this.getStation(949)?.let{ station ->
                searchResultList.add(station)
            }
        }
        else {
            for (line in lineList)
                line.searchStationInLine(stationName)?.let{ station ->
                    searchResultList.add(station)
                }
        }

        return searchResultList
    }

    /** 검색한 String을 포함한 모든 Staion List를 반환하는 함수입니다.*/
    fun searchStationsWithName(stringName: String): MutableList<Station> {
        val resultStationList: MutableList<Station> = mutableListOf()
        for (line in lineList) {
            for (station in line.stationList){
                // 각각의 Station객체의 stationName이 stringName으로 시작한다면
                // resultStationList에 추가
                if(station.stationName.startsWith(stringName))
                    resultStationList.add(station)
            }
        }
        return resultStationList
    }

    /** Subway 객체의 모든 Station 객체들을 반환하는 함수 */
    fun returnStations(): MutableList<Station>{
        val subwayStationList: MutableList<Station> = mutableListOf()
        for (line in lineList)
            subwayStationList.addAll(line.stationList)
        return subwayStationList
    }

}