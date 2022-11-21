package com.example.subway_alarm.model.repository

import android.graphics.PointF
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class StationPositionRepository {
    val database = Firebase.database
    val userRef = database.getReference("position")

    fun observePos(pos: NonNullMutableLiveData<PointF>){
        userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun postPos(newPos: PointF){
        userRef.setValue(newPos)
    }

}