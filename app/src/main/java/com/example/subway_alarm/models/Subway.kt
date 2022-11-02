package com.example.subway_alarm.models

object Subway {
    val lines: MutableList<Line> = mutableListOf()

    init {
        println("Subway 생성")
    }

    /**
     * Subway에서 특정 지하철역을 찾는 함수
     * 일단은 정확하게 입력해야지만 지하철역을 반환해준다.
     * Line은 고려 안하고 처음에 찾은 Station 클래스를 반환해준다.
     * 예외적으로 만약 "양평"을 검색할 시 list 형태로 [양평(5호선), 양평(중앙성)]을 반환해준다.
     * 못 찾는다면 null 반환!
     */
    fun searchStations(stationName: String): MutableList<Station>?{
        val stationList: MutableList<Station> = mutableListOf()
        if(stationName == "양평"){
            stationList.add(lines[4].stations[512])     // 양평(5호선)
            stationList.add(lines[9].stations[1049])    // 양평(경의중앙선)
            return stationList
        }
        for (line in lines) {
            val tempStation: Station? = line.searchStationInLine(stationName)
            if (tempStation != null) {
                stationList.add(tempStation)
                return stationList
            }
        }
        return null
    }

    fun addLines(line: Line) {
        lines.add(line)
    }
}