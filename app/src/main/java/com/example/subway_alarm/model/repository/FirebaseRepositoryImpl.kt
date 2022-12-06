package com.example.subway_alarm.model.repository

import android.graphics.PointF
import kotlin.math.*
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Subway
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRepositoryImpl : FirebaseRepository {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    init{
        getEndPointList()
    }

    override suspend fun postSelectedId(width: Int, height: Int, statusBarHeight: Int, selectedPos: PointF,
        scale: Float, transValue: PointF, stationId: NonNullMutableLiveData<Int>) {
        withContext(Dispatchers.IO) {
            // Pixel 4 API 32를 기준 x좌표로 변환
            val originX: Float =
                1080f * ((selectedPos.x / scale) + (width * (scale - 1f) / (2f * scale)) - transValue.x) / width

            // Pixel 4 API 32를 기준 y좌표로 변환
            val tempY: Float =
                (((selectedPos.y - statusBarHeight) / scale) + (height * (scale - 1f) / (2f * scale)) - transValue.y)
            // 2170 = 2280 - 44(navigationBarHeight) - 66(statusBarHeight), 1151 = 2170 / 2 + 66
            val originY: Float =
                1151f - (2170f * (((height / 2f) - tempY) / height)) // 중앙을 기준으로 y좌표를 계산한다.

            db.collection("stationId")
                .get()
                .addOnSuccessListener { result ->
                    val possibleStationMap: MutableMap<Float, Int> = mutableMapOf()
                    for (document in result) {
                        val x = document.data.get("x")?.toString()?.toFloat() ?: 0f
                        val y = document.data.get("y")?.toString()?.toFloat() ?: 0f

                        if (originX in (x - 10f) .. (x + 10f))
                            if (originY in (y - 10f) .. (y + 10f)) {
                                val distance = hypot((originX - x), (originY - y))
                                possibleStationMap[distance] = document.id.toInt()
                            }
                    }

                    val id = possibleStationMap.keys.minOrNull()?.let{
                        possibleStationMap.get(it)
                    } ?: 0
                    stationId.postValue(id)
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
                        document.data.get("id")?.toString()?.toInt()
                            ?.let {
                                // 즐겨찾기 등록
                                Subway.getStation(it)?.let { searchResult ->
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
                    Subway.getStation(stationId)?.let {
                        it.isFavorited = false
                        println("$stationId 를 즐겨찾기에서 해제했습니다.")
                    }
                }
                .addOnFailureListener {
                    println("firebase 에서 즐겨찾기 해제하는데 실패했습니다.")
                }
        }
    }

    /** firebase에서 각 호선에 대한 종착역 리스트를 받아오는 함수 */
    override fun getEndPointList() {
        db.collection("subwayEndPointList")
            .get()
            .addOnSuccessListener { result ->
                val endPointMap: MutableMap<Int, ArrayList<String>> = mutableMapOf()
                for (document in result) {
                    // @Suppress("UNCHECKED_CAST")
                    val firebaseList = document.data["종착역"] as? ArrayList<*> ?: arrayListOf<String>()
                    val temp: ArrayList<String> = arrayListOf()
                    for (item in firebaseList){
                        if(item is String) temp.add(item)
                    }
                    endPointMap[document.id.toInt()] = temp
                }
                Subway.initLines(endPointMap)
            }
            .addOnFailureListener { exp ->
                println(exp)
            }
    }
}