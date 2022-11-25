package com.example.subway_alarm.model.repository

import android.graphics.PointF
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationPositionRepository: FirebaseRepository{
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun postSelectedId(selectedPos: PointF, scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>) {
       withContext(Dispatchers.IO) {
           println("position이 repository에 post 되었습니다!")
           var id: Int = 0
           val originX: Float = (selectedPos.x / scale) + (1080 * (scale - 1) / (2 * scale) ) - transValue.x
           val originY: Float = (selectedPos.y / scale) + (2280 * (scale - 1) / (2 * scale) ) - transValue.y
            println("originX: $originX, originY: $originY")
            db.collection("stationId")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val x = document.data.get("x").toString().toFloat()
                        val y = document.data.get("y").toString().toFloat()
                        println("x : $x, y: $y")
                        if( (x - 50.0f) < originX  && (originX < x  + 50.0f)){
                            if ((y - 50.0f) <originY  && originY < (y + 50.0f) ){
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