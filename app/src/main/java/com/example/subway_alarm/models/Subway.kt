package com.example.subway_alarm.models

object Subway {
    val lines: MutableList<Line> = mutableListOf()

    init {
        println("Subway 생성")
    }

    fun addLines(line: Line) {
        lines.add(line)
    }
}