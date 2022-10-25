package com.example.subway_alarm.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.api.StationApiStorage
import com.example.subway_alarm.data.repository.StationRepository

class ViewModelImpl(
    var stationRepository: StationRepository
): ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String>
        get() = _data

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
        _data.value = "초기값"
    }

    /* view에서 준 input값으로 데이터를 변경
    * Repository에 있는 데이터를 불러옵니다 */
    fun updateData(stationName: String) {
        _data.value = stationRepository.getStationArrivals(stationName)
    }

}