package com.example.subway_alarm.model.repository

import android.graphics.PointF
import com.example.subway_alarm.extensions.NonNullMutableLiveData

interface FirebaseRepository {
    suspend fun postSelectedId(selectedPos: PointF, scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>)

}