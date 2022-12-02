package com.example.subway_alarm.viewModel

import androidx.lifecycle.viewModelScope
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

class AlarmViewModel: BaseViewModel() {
    private val _alarmTime = NonNullMutableLiveData<Int>(0)
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null

    val alarmTime: NonNullLiveData<Int>
    get() = _alarmTime

    /** 알람을 초기 설정하는 함수입니다. */
    fun setAlarm(time: Int) {
        _alarmTime.value = time
        countTime()
    }

    /** 이미 알람이 설정되었을 때, 재설정을 위해 호출하는 함수입니다. */
    fun resetAlarm(newTime: Int) {
        timerTask?.cancel()
        _alarmTime.value = newTime
        countTime()
    }

    // 설정한 시간을 보다 정확하게 count합니다.
    private fun countTime() {
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                if(_alarmTime.value <= 0) {
                    this.cancel()
                }
                if(_alarmTime.value > 0)
                    _alarmTime.postValue(_alarmTime.value - 1)
            }
        }
        timer?.schedule(timerTask,0,1000)
    }
}