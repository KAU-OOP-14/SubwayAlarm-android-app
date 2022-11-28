package com.example.subway_alarm.viewModel

import android.graphics.PointF
import androidx.core.graphics.minus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.repository.StationPositionRepository
import kotlinx.coroutines.launch

class PositionViewModel(
    private val stationPositionRepository: StationPositionRepository
) : ViewModel(){
    private val _pos = NonNullMutableLiveData<PointF>(PointF(0f,0f))        // 처음 터치 했을 때 바뀌는 데이터
    private val _movePos = NonNullMutableLiveData<PointF>(PointF(0f,0f))    // 움직일 때 바뀌는 데이터
    private val _selectedPos = NonNullMutableLiveData<PointF>(PointF(0f,0f)) // 역선택할 떄 바뀌는 데이터


    private val _isMoving = NonNullMutableLiveData<Boolean>(false)
    private val _state = NonNullMutableLiveData<Boolean>(false )
    private val _stationId = NonNullMutableLiveData<Int>(0)

    private var widthPixels: Int = 0
    private var heightPixels: Int = 0
    private var statusBarHeight: Int = 0
    private var navigationBarHeight: Int = 0

    var scaleValue: Float = 4.0f
    var isScaleChanged: Boolean = false
    var value = PointF(0f, 0f)
    var transValue: PointF = PointF(0f,0f)          // move한 좌표마다 처음 찍은 좌표와의 거리
    var totalTransValue: PointF = PointF(0f,0f)     // scale이 1이라고 가정할 때 움직인 거리

    val pos: NonNullLiveData<PointF>
        get() = _pos
    val movePos : NonNullLiveData<PointF>
        get() = _movePos
    val selectedPos : NonNullLiveData<PointF>
        get() = _selectedPos
    val isMoving : NonNullLiveData<Boolean>
        get() = _isMoving
    val state  : NonNullLiveData<Boolean>
        get() = _state
    val stationId: NonNullLiveData<Int>
        get() = _stationId

    // var isSelected: Boolean = false

    // 처음 터치가 일어났을 때 호출되는 함수
    private fun modifyPos(newPos: PointF){
        if(_state.value) {
            _pos.value = newPos
        }
    }
    fun setPos(newPos: PointF){
        modifyPos(newPos)
    }

    // 터치 이후 움직일 때 호출되는 함수
    private fun modifyMovePos(movePos: PointF){
        if(_state.value) {
            // transvalue는 현재 scale에 맞춰서 이미지를 이동시킨다.
            if (isScaleChanged) {
                totalTransValue.x = value.x / scaleValue
                totalTransValue.y = value.y / scaleValue
                isScaleChanged = false
            }
            //transValue = PointF(totalTransValue.x * scaleValue , totalTransValue.y * scaleValue) - (_pos.value - movePos)
            transValue = value - (_pos.value - movePos)
            _movePos.value = movePos
            if(!_isMoving.value){   // 움직인 이후 손을 뗀 경우로 총 움직인 거리를 최신화한다.
                value = transValue
                totalTransValue -= PointF((_pos.value - movePos).x / scaleValue, (_pos.value - movePos).y / scaleValue)
            }
        }
    }
    fun setMovePos(movePos: PointF){
        modifyMovePos(movePos)
    }

    // 역 선택에 대한 터치가 끝난 후 호출되는 함수
    private fun modifySelectedPos(selectedPos: PointF){
        if(_state.value) {
            _selectedPos.value = selectedPos
            viewModelScope.launch {
                stationPositionRepository.postSelectedId(widthPixels, heightPixels, statusBarHeight,
                    selectedPos, scaleValue, totalTransValue, _stationId)
            }
        }
    }
    fun setSelectedPos(selectedPos: PointF){
        modifySelectedPos(selectedPos)
    }

    // 터치 후 움직이고 있는지
    private fun changeMoving(newValue: Boolean){
        if(_state.value) {
            _isMoving.value = newValue
        }
    }
    fun setMoving(newValue: Boolean){
        changeMoving(newValue)
    }

    // state를 정의해서entryFragment에서 다른 Fragment로 전환할 시
    // position 변경되지 않도록 한다
    private fun changeState(newState: Boolean){
        _state.value = newState
    }
    fun setState(newState: Boolean){
        changeState(newState)
    }

    // 불필요한 observe 방지
    private fun changeStationId(newId: Int){
        _stationId.value = newId
    }
    fun setStationId(newId: Int){
        changeStationId((newId))
    }

    private fun resetTransValue(){
        value = PointF(0f, 0f)
        transValue = PointF(0f,0f)          // move한 좌표마다 처음 찍은 좌표와의 거리
        totalTransValue = PointF(0f,0f)
    }
    fun setTransValue(){
        resetTransValue()
    }

    fun setPixels(width: Int, height: Int, statusBarHeight: Int, navBarHeight: Int){
        widthPixels = width
        heightPixels = height
        this.statusBarHeight = statusBarHeight
        this.navigationBarHeight = navBarHeight
    }
}