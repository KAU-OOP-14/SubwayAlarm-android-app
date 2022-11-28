package com.example.subway_alarm.model.repository

import android.graphics.PointF
import android.widget.Toast
import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationPositionRepository{
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun postSelectedId(width: Int, height: Int, statusBarHeight: Int,
                               selectedPos: PointF, scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>) {
       withContext(Dispatchers.IO) {
           println("position이 repository에 post 되었습니다!")
           var id: Int = 0
           val originX: Float = 1080f * ((selectedPos.x / scale) + (width * (scale - 1f) / (2f * scale) ) - transValue.x) / width
           val tempY: Float = (((selectedPos.y - statusBarHeight)  / scale) + (height * (scale - 1f) / (2f * scale) ) - transValue.y)
           println("tempY: $tempY") // scale이 1일 때 터치된 y 좌표(상태바의 크기만큼 뺀 값)
           val originY: Float =   1151f - (2170f * (((height / 2f) - tempY) / height)) // Pixel 4 API 32를 기준의 좌표로 변환
           println("originX : $originX, originY: $originY")
           db.collection("stationId")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val x = document.data.get("x").toString().toFloat()
                        val y = document.data.get("y").toString().toFloat()
                        println("x : $x, y: $y")
                        if( (x - 20.0f) < originX  && (originX < x  + 20.0f)){
                            if ((y - 20.0f) < originY  && originY < (y + 20.0f) ){
                                id = document.id.toInt()
                                stationId.postValue(id)
                                break
                            }
                        }

                    }
                }
                .addOnFailureListener {
                    println("excep! : $it")
                }
        }
    }

}