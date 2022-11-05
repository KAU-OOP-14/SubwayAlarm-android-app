package com.example.subway_alarm.model.repository

import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway


/**
 * 즐겨찾기나 알람설정을 해 놓은 Station을 저장하는 저장소입니다.
 */
class StationRepositoryImpl : StationRepository {
    /**
     * application의 생명주기 동안 살아있어야 하는 livedata
     */
    private val subway: MutableLiveData<Subway> = MutableLiveData<Subway>()

    /**
     * 노선도 클릭이나, 검색, 즐겨찾기를 통해 불러온 current station 입니다.
     */
    override var curStation: Station = Station("초기값",0, mutableListOf())
    override var searchResultList: MutableList<Station> = mutableListOf()
    override var favoritStations: MutableList<Station> = mutableListOf()

    override fun search(stationName: String){
        searchResultList.clear()     // serachResultList 초기화
        if(stationName == "양평"){
            searchResultList.add(Subway.lines[4].stations[12])
            searchResultList.add(Subway.lines[9].stations[49])
            return
        }
        for(line in Subway.lines){
            val tempStation: Station? = line.searchStationInLine(stationName)
            if(tempStation != null){
                searchResultList.add(tempStation)
                return
            }
        }
        return
    }

    init {
        subway.value = SubwayBuilder.initSubway()
        println("Subway 객체 -> 저장소에 저장됨")
    }


}