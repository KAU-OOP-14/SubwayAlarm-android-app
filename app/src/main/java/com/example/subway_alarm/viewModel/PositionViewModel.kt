package com.example.subway_alarm.viewModel

import android.annotation.SuppressLint
import android.graphics.PointF
import androidx.core.graphics.minus
import androidx.lifecycle.viewModelScope
import com.example.subway_alarm.di.SubwayAlarmApp
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.repository.FirebaseRepository
import kotlinx.coroutines.launch

@SuppressLint("DiscouragedApi", "InternalInsetResource")
class PositionViewModel(
    private val stationPositionRepository: FirebaseRepository
) : BaseViewModel() {
    private val _pos = NonNullMutableLiveData<PointF>(PointF(0f, 0f))        // 처음 터치 했을 때 바뀌는 데이터
    private val _movePos = NonNullMutableLiveData<PointF>(PointF(0f, 0f))    // 움직일 때 바뀌는 데이터
    private val _selectedPos = NonNullMutableLiveData<PointF>(PointF(0f, 0f))// 역선택할 떄 바뀌는 데이터
    private val _scaleValue = NonNullMutableLiveData<Float>(4.0f)            // zoom in, out 할 때 바뀌는 데이터
    private val _stationId = NonNullMutableLiveData<Int>(0)                  // repository에서 반환된 결과 저장

    val pos: NonNullLiveData<PointF>
        get() = _pos
    val movePos: NonNullLiveData<PointF>
        get() = _movePos
    val selectedPos: NonNullLiveData<PointF>
        get() = _selectedPos
    val scaleValue: NonNullLiveData<Float>
        get() = _scaleValue
    val stationId: NonNullLiveData<Int>
        get() = _stationId

    private val display = SubwayAlarmApp.instance.resources.displayMetrics
    private val widthPixels: Int             // 디바이스의 가로 pixel 수
    private val statusBarHeight: Int        // 디바이스의 상태바 pixel 수
    private val navigationBarHeight: Int    // 디바이스의 네이게이션 pixel 수

    private var state: Boolean = false           // Mainactivity의 onTouch값을 받을 지 말지 결정
    private var isMoving: Boolean = false
    private var isScaleChanged: Boolean = false         // zoomIn, zoomOut이 일어났는가 체크하는 변수
    private var currentScaleTransValue = PointF(0f, 0f)           // scale에 상관없이 현재까지 move한 거리
    private var totalTransValue: PointF = PointF(0f, 0f)     // scale이 1이라고 가정할 때 현재까지 움직인 거리

    var transValue: PointF = PointF(0f, 0f)   // 한번 move했을 때 view가 움직일 거리
        private set
    var heightPixels: Int                            // 디바이스의 세로 pixel 수
        private set

    init{
        // dispaly의 픽셀 수 구하기
        // height는 상단의 상태 바와 하단의 navigationBar 크기를 제외한 픽셀 수가 나온다.
        widthPixels = display.widthPixels
        heightPixels = display.heightPixels
        var statusBarHeight = 0
        var resourceId = SubwayAlarmApp.instance.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = SubwayAlarmApp.instance.resources.getDimensionPixelSize(resourceId)
        }

        var navigationBarHeight = 0
        resourceId = SubwayAlarmApp.instance.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            navigationBarHeight = SubwayAlarmApp.instance.resources.getDimensionPixelSize(resourceId)
        }
        this.statusBarHeight = statusBarHeight
        this.navigationBarHeight = navigationBarHeight
    }

    // 처음 터치가 일어났을 때 호출되는 함수
    private fun modifyPos(newPos: PointF) {
        if (state) _pos.value = newPos
    }

    fun setPos(newPos: PointF) {
        modifyPos(newPos)
    }

    // 터치 이후 움직일 때 호출되는 함수
    private fun modifyMovePos(movePos: PointF) {
        if (state) {
            if (isScaleChanged) {
                totalTransValue.x = currentScaleTransValue.x / _scaleValue.value
                totalTransValue.y = currentScaleTransValue.y / _scaleValue.value
                isScaleChanged = false
            }
            // transvalue는 현재 scale에 맞춰서 이미지를 이동시킨다.
            transValue = currentScaleTransValue - (_pos.value - movePos)
            if (!isMoving) {   // 움직인 이후 손을 뗀 경우로 총 움직인 거리를 최신화한다.
                val tempTotalTransValue = totalTransValue - PointF(
                    (_pos.value - movePos).x / _scaleValue.value,
                    (_pos.value - movePos).y / _scaleValue.value
                )

                // width / 2f + width / 14f = 4f * width / 7f
                if (tempTotalTransValue.x !in (-(widthPixels / 2f)) .. (widthPixels / 2f)){
                    totalTransValue.y = tempTotalTransValue.y
                    transValue.x = currentScaleTransValue.x
                }
                // 2280 : 900 = height : x -> 0.4 * height = x -> (height/2f - 0.4 * height) = height / 10f
                else if(tempTotalTransValue.y !in (-(heightPixels / 10f)) .. (heightPixels / 10f)){
                    totalTransValue.x = tempTotalTransValue.x
                    transValue.y = currentScaleTransValue.y
                }
                else totalTransValue = tempTotalTransValue
                currentScaleTransValue = transValue
            }
            _movePos.value = movePos

        }
    }

    fun setMovePos(movePos: PointF) {
        modifyMovePos(movePos)
    }

    // 역 선택에 대한 터치가 끝난 후 호출되는 함수
    private fun modifySelectedPos(selectedPos: PointF) {
        if (state) {
            _selectedPos.value = selectedPos
            viewModelScope.launch {
                stationPositionRepository.postSelectedId(
                    widthPixels, heightPixels, statusBarHeight,
                    selectedPos, _scaleValue.value, totalTransValue, _stationId
                )
            }
        }
    }

    fun setSelectedPos(selectedPos: PointF) {
        modifySelectedPos(selectedPos)
    }

    // 터치 후 움직이고 있는지
    private fun changeMoving(newState: Boolean) {
        if (state) isMoving = newState
    }

    fun setMoving(newState: Boolean) {
        changeMoving(newState)
    }

    // state를 정의해서 entryFragment에서 다른 Fragment로 전환할 시
    // MainActivity의 onTouch가 반영되지 않도록 한다.
    private fun changeState(newState: Boolean) {
        state = newState
    }

    fun setState(newState: Boolean) {
        changeState(newState)
    }

    // 불필요한 observe로 인한 버그 발생 방지를 위해
    // Fragment 전환 시 stationId를 0으로 초기화 할 때 사용
    private fun changeStationId(newId: Int) {
        if(newId != 0){
            val viewCenterPos = PointF(widthPixels/2f, heightPixels/2f)
            transValue = currentScaleTransValue - (_pos.value - viewCenterPos)
            currentScaleTransValue = transValue
            totalTransValue -= PointF(
                (_pos.value - viewCenterPos).x / _scaleValue.value,
                (_pos.value - viewCenterPos).y / _scaleValue.value)
            }
        _stationId.value = newId
    }
    fun setStationId(newId: Int) {
        changeStationId((newId))
    }

    private fun modifyTranslationValue(value: PointF, transValue: PointF, totalTransValue: PointF) {
        this.currentScaleTransValue = value
        this.transValue = transValue
        this.totalTransValue = totalTransValue
    }
    // Fragment 전환 시 지금까지 움직인 거리 초기화
    fun resetTranslationValue() {
        modifyTranslationValue(PointF(0f, 0f), PointF(0f, 0f), PointF(0f, 0f))
    }

    private fun modifyScaleValue(newScaleValue: Float){
        val tempScaleValue = _scaleValue.value + newScaleValue
        if(tempScaleValue in 2f .. 6f) _scaleValue.value = tempScaleValue
        isScaleChanged = true
    }

    fun onZoomIn() {
        modifyScaleValue(2f)
    }

    fun onZoomOut() {
        modifyScaleValue(-2f)
    }
}