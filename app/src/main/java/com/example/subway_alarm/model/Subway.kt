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

    fun addLines(line: Line){
        line.endPointList.addAll(edgeList[line.lineId - 1])
        line.setStations()
        lines.add(line)
    }
}