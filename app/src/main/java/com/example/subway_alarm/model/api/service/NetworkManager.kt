package com.example.subway_alarm.model.api.service

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkManager {
    private val retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://swopenapi.seoul.go.kr/")
            .build()
    val subwayApi = retrofit.create(NetworkService::class.java)
}