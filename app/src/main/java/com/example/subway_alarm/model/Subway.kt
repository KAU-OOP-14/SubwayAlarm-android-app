package com.example.subway_alarm.model


object Subway {
    /** station 역 id 단위입니다. */
    const val STATION_ID_UNIT = 1000
    private val lineList: ArrayList<Line> = arrayListOf()

    init {
        println("Subway 생성")
    }

    /** SubwayBuilder에서 Subway 객체에 Line 객체들을 초기화 해주는 함수 */
    fun addLines(lineList: ArrayList<Line>){
        for (line in lineList){
            //line.initEndPointList(subwayEndPointList[line.lineId - 1])
            //line.initNearStations()
            this.lineList.add(line)
        }
    }

    // firebase에서 endPointList를 받아와서 초기화 해주는 함수
    fun initLines(endPointMap: MutableMap<Int, ArrayList<String>>){
        for (line in lineList){
            endPointMap[line.lineId]?.let{ array ->
                line.initEndPointList(array)
            } ?: arrayListOf<String>("오류")
            line.initNearStations()
        }
    }

    /** stationId를 이용해서 Station 객체를 반환하는 함수
     *  올바르지 않는 stationId(index 오류)이면 null 반환
     */
    fun getStation(stationId: Int): Station?{
        return try {
            lineList[(stationId / STATION_ID_UNIT) - 1].getStationInLine(stationId)
        }catch (e: IndexOutOfBoundsException) {
            println("index 오류 발생 ㅠㅠ : ${(stationId / STATION_ID_UNIT)} / $e")
            null
        }
    }

    /** 매개변수 stationName을 이름으로 가지는 역들을 검색하고 반환합니다 */
    fun searchStation(stationName: String): ArrayList<Station> {
        val searchResultList: ArrayList<Station> = arrayListOf()

        for (line in lineList)
            line.searchStationInLine(stationName)?.let { station ->
                searchResultList.add(station)
            }

        return searchResultList
    }

    /** 검색한 String을 포함한 모든 Staion List를 반환하는 함수입니다.*/
    fun searchStationsWithName(stringName: String): ArrayList<Station> {
        val resultStationList: ArrayList<Station> = arrayListOf()
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
    fun returnStations(): ArrayList<Station>{
        val subwayStationList: ArrayList<Station> = arrayListOf()
        for (line in lineList)
            subwayStationList.addAll(line.stationList)
        return subwayStationList
    }

}