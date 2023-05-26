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
    private val _pos = NonNullMutableLiveData<PointF>(PointF(0f, 0f))        // 처음 터치 했을 때 바뀌는 live 데이터
    private val _movePos = NonNullMutableLiveData<PointF>(PointF(0f, 0f))    // 움직일 때 바뀌는 live 데이터
    private val _scaleValue = NonNullMutableLiveData<Float>(4.0f)            // zoom in, out 할 때 바뀌는 live 데이터
    private val _stationId = NonNullMutableLiveData<Int>(0)                  // repository에서 반환된 결과 저장하는 live 데이터

    val pos: NonNullLiveData<PointF>
        get() = _pos
    val movePos: NonNullLiveData<PointF>
        get() = _movePos
    val scaleValue: NonNullLiveData<Float>
        get() = _scaleValue
    val stationId: NonNullLiveData<Int>
        get() = _stationId

    // dispaly의 픽셀 수 구하기
    // height는 상단의 상태 바와 하단의 navigationBar 크기를 제외한 픽셀 수가 나온다.
    private val display = SubwayAlarmApp.instance.resources.displayMetrics
    private val widthPixels = display.widthPixels                       // 디바이스의 가로 pixel 수
    private val statusBarHeight: Int                                    // 디바이스의 상태바 pixel 수
    private val navigationBarHeight: Int                                // 디바이스의 navigationBar의 pixel 수

    private var state: Boolean = false                                  // Mainactivity의 onTouch값을 받을 지 말지 결정
    private var isMoving: Boolean = false                               // 드래그 중인지 상태를 체크하는 변수
    private var isScaleChanged: Boolean = false                         // zoomIn, zoomOut이 일어났는가 체크하는 변수
    private var currentScaleTransValue = PointF(0f, 0f)            // scale에 상관없이 현재까지 move한 거리
    private var totalTransValue = PointF(0f, 0f)                   // scale이 1이라고 가정할 때 현재까지 움직인 거리

    // 밖에서는 볼 수 있지만, 변경 불가능하도록 변경
    var transValue: PointF = PointF(0f, 0f)                        // 한번 move했을 때 view가 움직일 거리
        private set
    var heightPixels: Int  = display.heightPixels                       // 디바이스의 세로 pixel 수
        private set

    init{
        // dispaly의 픽셀 수 구하기
        // height는 상단의 상태 바와 하단의 navigationBar 크기를 제외한 픽셀 수가 나온다.
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
        if (state) {
            // 만약 화면 비율이 변경 되었다면 그 전 비율에서 움직인 거리를 scale이 1일 때의 거리로 변환
            if (isScaleChanged) {
                totalTransValue.x = currentScaleTransValue.x / _scaleValue.value
                totalTransValue.y = currentScaleTransValue.y / _scaleValue.value
                isScaleChanged = false
            }
            _pos.value = newPos
        }
    }

    fun setPos(newPos: PointF) {
        modifyPos(newPos)
    }

    // 터치 이후 움직일 때 호출되는 함수
    private fun modifyMovePos(movePos: PointF) {
        if (state) {
            // transvalue는 현재 scale에 맞춰서 이미지를 이동해야하는 값을 가진다.
            transValue = currentScaleTransValue - (_pos.value - movePos)
            if (!isMoving) {   // 움직인 이후 손을 뗀 경우로 총 움직인 거리를 기록한다.
                val tempTotalTransValue = totalTransValue - PointF(
                    (_pos.value - movePos).x / _scaleValue.value,
                    (_pos.value - movePos).y / _scaleValue.value
                )

                // 기록하기 전 드래그 범위를 벗어났는지 확인하고,
                // 벗어났으면 그 방향으로 이동하지 않는다.
                if (tempTotalTransValue.x !in (-(widthPixels / 2f)) .. (widthPixels / 2f)){
                    totalTransValue.y = tempTotalTransValue.y
                    transValue.x = currentScaleTransValue.x
                }
                else if(tempTotalTransValue.y !in (-(heightPixels / 8f)) .. (heightPixels / 8f)){
                    totalTransValue.x = tempTotalTransValue.x
                    transValue.y = currentScaleTransValue.y
                }
                else totalTransValue = tempTotalTransValue

                currentScaleTransValue = transValue         // currentScaleTransValue를 transValue로 변경
            }
            _movePos.value = movePos

        }
    }

    fun setMovePos(movePos: PointF) {
        modifyMovePos(movePos)
    }

    /** 역 선택에 대한 터치가 끝난 후 호출되는 함수 */
    private fun findSelectedPosId(newSelectedPos: PointF) {
        if (state) {
            // 선택한 좌표와 디바이스 정보들을 stationPositionRepository에 보낸다
            viewModelScope.launch {
                stationPositionRepository.postSelectedId(
                    widthPixels, heightPixels, statusBarHeight,
                    newSelectedPos, _scaleValue.value, totalTransValue, _stationId
                )
            }
        }
    }

    fun setSelectedPos(selectedPos: PointF) {
        findSelectedPosId(selectedPos)
    }

    /** 터치 후 움직이고 있는지 세팅해주는 함수 */
    private fun modifyMoving(newState: Boolean) {
        if (state) isMoving = newState
    }

    fun setMoving(newState: Boolean) {
        modifyMoving(newState)
    }

    /** state를 정의해서 entryFragment에서 다른 Fragment로 전환할 시
     *  MainActivity의 onTouch가 반영되지 않도록 한다.
     */
    private fun modifyState(newState: Boolean) {
        state = newState
    }

    fun setState(newState: Boolean) {
        modifyState(newState)
    }

    /** stationId를 바꾸어 주는 함수
     *  id가 0이 아니면 역을 제대로 선택한 경우로 화면 중앙으로 이동시킨다.
     * */
    private fun modifyStationId(stationId: Int) {
        _stationId.value =  stationId
    }

    fun setStationId(stationId: Int) {
        modifyStationId((stationId))
    }

    /** transValue들을 초기화해주는 함수 */
    private fun modifyTranslationValue(value: PointF, transValue: PointF, totalTransValue: PointF) {
        this.currentScaleTransValue = value
        this.transValue = transValue
        this.totalTransValue = totalTransValue
    }
    fun resetTranslationValue() {
        // 지금까지 움직인 거리를 0으로 초기화
        modifyTranslationValue(PointF(0f, 0f), PointF(0f, 0f), PointF(0f, 0f))
    }

    /** 들어온 scaleValue를 가지고 확대,축소하는 함수 */
    private fun modifyScaleValue(inputScaleValue: Float){
        val tempScaleValue = _scaleValue.value + inputScaleValue
        // 들어온 scaleValue를 반영한 값이 2f .. 6f 범위 안에 있다면 그 값을 대입
        if(tempScaleValue in 2f .. 6f) _scaleValue.value = tempScaleValue
        isScaleChanged = true
    }

    fun onZoomIn() {
        // 확대
        modifyScaleValue(2f)
    }

    fun onZoomOut() {
        // 축소
        modifyScaleValue(-2f)
    }

    /** view에서 repository에서 올바른 stationId 값이 넘어 온것을
     *  확인하고 이 함수를 호출함으로 이동해야할 값을 계산하고 이동한다.
     */
    fun modifyTransValue(){
        val viewCenterPos = PointF(widthPixels/2f, heightPixels/2f)
            transValue = currentScaleTransValue - (_pos.value - viewCenterPos)
            currentScaleTransValue = transValue
            totalTransValue -= PointF(
                (_pos.value - viewCenterPos).x / _scaleValue.value,
                (_pos.value - viewCenterPos).y / _scaleValue.value)
    }
}