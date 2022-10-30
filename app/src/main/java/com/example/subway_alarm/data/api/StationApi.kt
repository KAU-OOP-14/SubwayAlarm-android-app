package com.example.subway_alarm.data.api

interface StationApi {
    fun setStringApiData(stringApiData: String)
    fun getApiData(): Array<String?>
    fun setParseApiData(apiStringData: String?)
}
