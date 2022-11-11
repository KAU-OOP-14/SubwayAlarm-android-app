package com.example.subway_alarm.model.repository

import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.api.dataModel.ApiModelList
import io.reactivex.rxjava3.core.Single

interface StationRepository {
    /** 현재 main thread에서 관찰하고 있는 station입니다. */
    var curStation: Station
    /** 검색 결과에 대한 Map container를 가지는 변수입니다. */
    val afterSearchedResultMap: MutableMap<Int, String>

    /** 검색에 대한 결과로 station list를 반환합니다. */
    var searchResultList: MutableList<Station>

    /** 즐겨찾기중인 station의 리스트입니다. */
    var favoriteStations: MutableList<Station>

    /** 전달받은 역 이름으로 역을 검색해서 searchResults에 업데이트합니다. */
    fun search(stationName: String)

    /** Retrofit에서 api 데이터를 받아옵니다. */
    fun retrofitGetArrivals(stationName: String): Single<ApiModelList>

    /** 검색한 String을 포함하는 모든 Station의 Id와 이름을 Map Container로 반환하는 함수입니다.*/
    fun searchAndReturnMap(stationName: String): MutableMap<Int, String>

    fun getCrossedLine(direction: String): Array<String>?
}