package com.example.subway_alarm.model.repository

import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Subway
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StationPositionRepository : FirebaseRepository {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun postSelectedId(width: Int, height: Int, statusBarHeight: Int, selectedPos: PointF,
        scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>) {
        withContext(Dispatchers.IO) {
            println("position이 repository에 post 되었습니다!")
            var id: Int = 0

            // Pixel 4 API 32를 기준 x좌표로 변환
            val originX: Float =
                1080f * ((selectedPos.x / scale) + (width * (scale - 1f) / (2f * scale)) - transValue.x) / width

            // Pixel 4 API 32를 기준 y좌표로 변환
            val tempY: Float =
                (((selectedPos.y - statusBarHeight) / scale) + (height * (scale - 1f) / (2f * scale)) - transValue.y)
            println("tempY: $tempY") // scale이 1일 때 터치된 y좌표에서 상태바 height를 뺀 값
            // 2170 = 2280 - 44(navigationBarHeight) - 66(statusBarHeight), 1151 = 2170 / 2 + 66
            val originY: Float =
                1151f - (2170f * (((height / 2f) - tempY) / height)) // 중앙을 기준으로 y좌표를 계산한다.
            println("originX : $originX, originY: $originY")

            db.collection("stationId")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val x = document.data.get("x")?.toString()?.toFloat() ?: 0f
                        val y = document.data.get("y")?.toString()?.toFloat() ?: 0f
                        println("firebase position value x : $x, y: $y")
                        if ((x - 10.0f) < originX && (originX < x + 10.0f)) {
                            if ((y - 10.0f) < originY && originY < (y + 10.0f)) {
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
                                // 즐겨찾기 등록
                                Subway.searchWithId(it)?.let { searchResult ->
                                    stations.add(it)
                                    searchResult.isFavorited = true
                                }
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
                    println("성공적으로 firebase에 추가했습니다 : $stationId")
                }
                .addOnFailureListener {
                    println("firebase에 추가하는데 실패했습니다.. : $it")
                }
        }

    }

    override suspend fun deleteFavorite(stationId: Int) {
        withContext(Dispatchers.IO) {
            db.collection("favorites")
                .document(stationId.toString()).delete()
                .addOnSuccessListener {
                    //즐겨찾기 해제
                    Subway.searchWithId(stationId)?.let {
                        it.isFavorited = false
                        println("$stationId 를 즐겨찾기에서 해제했습니다.")
                    }
                }
                .addOnFailureListener {
                    println("firebase 에서 즐겨찾기 해제하는데 실패했습니다.")
                }
        }
    }

    override suspend fun getEdgeList() {
    }
}