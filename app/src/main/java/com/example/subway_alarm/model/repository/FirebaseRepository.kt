package com.example.subway_alarm.model.repository

import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData

interface FirebaseRepository {
    suspend fun postSelectedId(width: Int, height: Int, statusBarHeight: Int,
                               selectedPos: PointF, scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>)

    /** firebase에서부터 즐겨찾기 목록을 가져옵니다. */
    suspend fun getFavorites(favorites: NonNullMutableLiveData<List<Int>>)

    /** firebase에 새로운 즐겨찾기를 등록합니다.*/
    suspend fun postFavorites(stationId: Int)

    /** firebase 즐겨찾기 목록에서 제거합니다. */
    suspend fun deleteFavorite(stationId: Int)

    /** 호선 별 edge list를 가져옵니다. */
    suspend fun getEdgeList()
}