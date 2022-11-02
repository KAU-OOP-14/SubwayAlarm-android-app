package com.example.subway_alarm.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subway_alarm.data.api.ApiThread
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.api.StationApiStorage
import com.example.subway_alarm.data.repository.StationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewModelImpl(
    private val stationRepository: StationRepository,
    private val stationApiStorage: StationApi,
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
        //val thread: ApiThread = ApiThread(stationApiStorage)
        val thread: ApiThread by inject()
        thread.setStationName(stationName)
        thread.start()
        thread.join()
        updateData()
    }

    /**
     * 사용자가 입력한 값으로 station을 검색하고, 걸과를 반영합니다.
     */
    fun searchStationWithInput(stationName: String) {

    }

    /**
     *Main Fragment에서 오른쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goRight() {
        //현재 station의 right node를 가져옵니다.

    }

    /**
     * Main Fragement에서 왼쪽 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun goLeft() {
        //현재 station의 left node를 가져옵니다.

        //새로운 api를 호출합니다.

        //live data를 갱신합니다.

    }

    /**
     * Main Fragment에서 알람 버튼을 눌렀을 때 호출하는 함수입니다.
     */
    fun setAlarm() {

    }

    fun getStationData(direction: String) {
        val curStation = stationRepository.getCurrentStation()
        if(curStation != null) {
            //val nextStation: Station? = curStation.getNode(direction)
        }


    }


    override fun onCleared() {
        super.onCleared()
        println("리소스 정리")
    }

}