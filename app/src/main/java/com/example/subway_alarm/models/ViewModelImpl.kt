package com.example.subway_alarm.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subway_alarm.data.api.ApiThread
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.repository.StationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelImpl(
    val stationRepository: StationRepository,
    val stationApiStorage: StationApi,
): ViewModel(), KoinComponent {
    private val _data = MutableLiveData<Array<String?>>()

    // Getter
    val data: LiveData<Array<String?>>
        get() = _data

    //초기값
    init {
        println("ViewModelImpl - 생성자 호출")
        _data.value = arrayOfNulls<String>(8)
    }

    /**
     * 스레드가 끝난 후, LiveData를 업데이트 합니다.
     * ApiThread에서 자동 호출 됩니다.
     */
    private fun updateData() {
        val testArr = stationApiStorage.getApiData()
        _data.value = testArr
    }

    /**
     * 새로운 api를 요청합니다.
     * StationApiStorage 에 저장됩니다.
     */
    fun requestApiData(stationName: String) {
        //새로운 스레드르 의존성 주입으로 생성합니다.
        val thread: ApiThread by inject()
        thread.setStationName(stationName)
        thread.start()
        thread.join()
        updateData()
    }


    override fun onCleared() {
        super.onCleared()
        println("리소스 정리")
    }

}