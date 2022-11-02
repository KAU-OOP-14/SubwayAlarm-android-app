package com.example.subway_alarm.data.db

import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.models.Line
import com.example.subway_alarm.models.Station
import com.example.subway_alarm.models.Subway

object SubwayBuilder {

    /**
     * Subway를 만들어줍니다.
     * Station Repository가 생성될 때 한번만 호출되는 함수입니다.
     */
    fun initSubway(): Subway{
        val lines: MutableList<Line> = mutableListOf()
        //Excel Thread에서 데이터를 추출합니다.
        val thread = ExcelThread()
        thread.start()
        thread.join()
        val db = thread.getdb()

        //16개 line을 생성합니다.
        for(lineId in 1..16) {
            lines.add(Line(lineId))
        }

        //db를 돌면서 station을 생성하고, line에 집어넣습니다.
        for(row in db) {
            val id: Int = row[1].substringBefore(".").toInt()
            val linelist: MutableList<Int> = mutableListOf()
            if(row[0].contains(".")){
                linelist.add(row[0].substringBefore('.').toInt())
            }
            else{
                val tempList = row[0].split(",")
                for (i in tempList){
                    linelist.add(i.toInt())
                }
            }
            lines[(id/100)-1].addStations(Station(row[2], id, linelist))
        }


        //station이 모두 들어간 뒤, line을 subway에 넣습니다.
        for(line in lines) {
            Subway.addLines(line)
        }

        return Subway
    }
}