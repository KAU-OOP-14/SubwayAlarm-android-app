package com.example.subway_alarm.model.repository

import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Subway
import com.google.firebase.database.DatabaseReference
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

    override suspend fun getFavorites(favorites: NonNullMutableLiveData<List<Int>>) {
        withContext(Dispatchers.IO) {
            db.collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    val stations: MutableList<Int> = mutableListOf()
                    for (document in result) {
                        val newId = document.data.get("id")?.toString()?.toInt()
                            ?.let {
                                stations.add(it)
                                // 즐겨찾기 등록
                                Subway.searchWithId(it).isFavorited = true
                            }

                    }
                    favorites.postValue(stations)
                    println("정상적으로 firebase에서 favorites를 불러왔습니다. ${stations.size}")
                }
                .addOnFailureListener {
                    println("favorites를 firebase에서 가져오지 못했습니다.. : $it")
                }
        }

    }

    override suspend fun postFavorites(stationId: Int) {
        val id = mapOf("id" to stationId)
        withContext(Dispatchers.IO) {
            db.collection("favorites").document(stationId.toString()).set(id)
                .addOnSuccessListener {
                    println("성공적으로 firebase에 저장했습니다 : $stationId")
                }
                .addOnFailureListener {
                    println("firebase에 저장하는데 실패했습니다.. : $it")
                }
        }

    }

    override suspend fun deleteFavorite(stationId: Int) {
        withContext(Dispatchers.IO) {
            db.collection("favorites")
                .document(stationId.toString()).delete()
                .addOnSuccessListener {
                    //즐겨찾기 해제
                    Subway.searchWithId(stationId).isFavorited = false
                    println("$stationId 를 즐겨찾기에서 해제했습니다.")
                }
                .addOnFailureListener {
                    println("firebase 에서 즐겨찾기 해제하는데 실패했습니다.")
                }
        }
    }
}