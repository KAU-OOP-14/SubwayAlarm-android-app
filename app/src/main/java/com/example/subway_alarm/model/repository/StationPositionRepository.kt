package com.example.subway_alarm.model.repository

import android.graphics.PointF
import com.example.subway_alarm.viewModel.listener.OnStationIdResult
import com.google.firebase.firestore.FirebaseFirestore

class StationPositionRepository(listener: OnStationIdResult){
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val userRef = db.collection("stationId")
    val callback = listener

    fun findObjectPos(pos: PointF, scale: Float, transX : Float = 0f, transY: Float = 0f){

        //1080 * 2280
        val originX: Float = (pos.x / scale) + (1080 * (scale - 1) / (2 * scale) ) - transX
        val originY: Float = (pos.y / scale) + (2280 * (scale - 1) / (2 * scale) ) - transY
        var id: Int = 0
        println("orginX : $originX, orginY : $originY")
        db.collection("stationId")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val x = document.data.get("x").toString().toFloat()
                    val y = document.data.get("y").toString().toFloat()
                    if( (x - 10.0f) < originX  && (originX < x  + 10.0f)){
                        if ((y - 10.0f) <originY  && originY < (y + 10.0f) ){
                            id = document.id.toInt()
                        }
                    }
                }
                println("id : $id")
                if(id != 0)
                    callback.onStationIdResult(id)
            }
            .addOnFailureListener{
                println("excep! : $it")
            }
    }


}