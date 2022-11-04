package com.example.subway_alarm.model

object Subway {
    val lines: MutableList<Line> = mutableListOf()
    val edgeList = arrayOf(
        listOf("소요산", "인천/신창", "인천", "광운대", "신창"),
        listOf("내선(시계)", "외선(반시계)", "내선/까치산", "까치산", "신도림", "외선/신설동", "성수", "신설동"),
        listOf("대화", "오금"),
        listOf("진접", "당고개"),
        listOf("방화", "하남검단산/마천", "마천", "하남검단산"),
        listOf("단선", "신내", "응암순환"),
        listOf("장암", "석남"),
        listOf("암사", "모란"),
        listOf("중앙보훈병원", "개화"),
        listOf("문산", "서울역/지평", "지평", "서울역"),
        listOf("서울역", "인천공항2터미널"),
        listOf("청량리", "춘천"),
        listOf("왕십리/청량리", "인천"),
        listOf("신사", "광교"),
        listOf("인천공항1터미널", "용유"),
        listOf("신설동", "북한산우이")
    )
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

    fun addLines(line: Line){
        line.endPointList.addAll(edgeList[line.lineId - 1])
        line.setStations()
        lines.add(line)
    }
}