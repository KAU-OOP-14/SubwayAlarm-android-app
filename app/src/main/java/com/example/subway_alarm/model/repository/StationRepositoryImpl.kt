package com.example.subway_alarm.model.repository

import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway


/** 즐겨찾기나 알람설정을 해 놓은 Station을 저장하는 저장소입니다.
 * 지속적으로 api 데이터를 갱신받고 싶은 Station을 저장합니다.
 */
class StationRepositoryImpl: StationRepository {
    //application의 생명주기 동안 살아있어야 하는 livedata
    private val subway: MutableLiveData<Subway> = MutableLiveData<Subway>()

    var curStation: Station?
    init {
        subway.value = SubwayBuilder.initSubway()
        println("Subway 객체 -> 저장소에 저장됨")
        curStation = null
    }


}