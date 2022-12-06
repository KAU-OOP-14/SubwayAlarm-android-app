package com.example.subway_alarm.model.repository

import android.graphics.PointF
import com.example.subway_alarm.extensions.NonNullMutableLiveData

interface FirebaseRepository {
    /** firebase에서부터 선택한 좌표의 역 아이디를 가져옵니다. */
    suspend fun postSelectedId(width: Int, height: Int, statusBarHeight: Int,
                               selectedPos: PointF, scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>)

    /** firebase에서부터 즐겨찾기 목록을 가져옵니다. */
    suspend fun getFavorites(favorites: NonNullMutableLiveData<List<Int>>)

    /** firebase에 새로운 즐겨찾기를 등록합니다.*/
    suspend fun postFavorites(stationId: Int)

    /** firebase 즐겨찾기 목록에서 제거합니다. */
    suspend fun deleteFavorite(stationId: Int)

    /** Repository가 생성될 때, 호선 별 종착역 list를 가져옵니다. */
    fun getEndPointList()
}