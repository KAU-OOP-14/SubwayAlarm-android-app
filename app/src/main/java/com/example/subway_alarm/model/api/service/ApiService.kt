package com.example.subway_alarm.model.api.service

import com.example.subway_alarm.model.api.dataModel.ApiModelList

interface ApiService {
    /**
     * 입력받은 이름으로 api를 요청합니다.
     */
    fun requestApi(stationName: String)

    /**
     * storage에 저장된 모든 페이지의 api를 반환합니다
     */
    fun getApiModelList(): ApiModelList
}