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

    fun setAlarm(time: Int) {
        _alarmTime.value = time
        countTime()
    }

    fun resetAlarm(newTime: Int) {
        timerTask?.cancel()
        _alarmTime.value = newTime
        countTime()
    }

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