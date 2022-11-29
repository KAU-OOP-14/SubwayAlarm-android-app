package com.example.subway_alarm.viewModel

import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData

class AlarmViewModel: BaseViewModel() {
    private val _alarmTime = NonNullMutableLiveData<Int>(0)

    val alarmTime: NonNullLiveData<Int>
    get() = _alarmTime

    fun setAlarm(time: Int) {
        _alarmTime.value = time
    }

    fun resetAlarm(newTime: Int) {
        _alarmTime.value = newTime
    }
}