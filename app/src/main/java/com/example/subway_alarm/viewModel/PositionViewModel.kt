package com.example.subway_alarm.viewModel

import android.graphics.PointF
import androidx.annotation.NonNull
import androidx.core.graphics.minus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.repository.StationPositionRepository

class PositionViewModel : ViewModel(){
    private val _pos = NonNullMutableLiveData<PointF>(PointF(0f,0f))
    private val _movePos = NonNullMutableLiveData<PointF>(PointF(0f,0f))
    private val _isMoving = NonNullMutableLiveData<Boolean>(false)
    private val _state = NonNullMutableLiveData<Boolean>(false )

    val pos: NonNullLiveData<PointF>
        get() = _pos
    val movePos : NonNullLiveData<PointF>
        get() = _movePos
    val isMoving : NonNullLiveData<Boolean>
        get() = _isMoving
    val state  : NonNullLiveData<Boolean>
        get() = _state

    var tempPos = PointF(0f, 0f)
    var isSelected: Boolean = false

    private fun modifyPos(newPos: PointF){
        if(state.value) {
            if(_isMoving.value)
                tempPos = _pos.value
            _pos.value = newPos
        }
    }

    fun setPos(newPos: PointF){
        modifyPos(newPos)
    }

    private fun modifyMovePos(newPos: PointF){
        if(state.value){
            _movePos.value = newPos
        }
    }

    fun setMovePos(newPos: PointF){
        modifyMovePos(newPos)
    }


    private fun changeMoving(newValue: Boolean){
        if(state.value) {
            _isMoving.value = newValue
        }
    }

    fun setMoving(newValue: Boolean){
        changeMoving(newValue)
    }

    // state를 정의해서entryFragment에서 다른 Fragment로 전환할 시
    // position 변경되지 않도록 한다
    private fun changeState(newValue: Boolean){
        _state.value = newValue
    }

    fun setState(newValue: Boolean){
        changeState(newValue)
    }
}