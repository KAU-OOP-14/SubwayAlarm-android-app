package com.example.subway_alarm.model.api.service

import com.example.subway_alarm.model.api.dataModel.ApiModelList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkService {
    @GET("api/subway/7a5370504e6868743131324443656265/json/realtimeStationArrival/0/5/{post}")
    fun getStationArrivals(@Path("post") post: String): Single<ApiModelList>
}
