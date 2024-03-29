package com.example.subway_alarm.viewModel

import androidx.lifecycle.viewModelScope
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.repository.FirebaseRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val firebaseRepository: FirebaseRepository
) : BaseViewModel() {
    private val _favorites = NonNullMutableLiveData<List<Int>>(listOf())
    val favorites: NonNullMutableLiveData<List<Int>>
        get() = _favorites


    /** fragment에서 즐겨찾기 버튼을 눌렀을 때 호출되는 함수입니다.*/
    fun onBookmarkClick(clickedId: Int) {
        viewModelScope.launch {
            getFavorites()
            val old = _favorites.value
            for (stationId in old) {
                if (stationId == clickedId) {
                    // 이미 favorites에 있으므로 즐겨찾기 해제합니다.
                    deleteFavorite(clickedId)
                    this.cancel()
                }
            }
            // favorites에 없다면 즐겨찾기에 추가합니다.
            addFavorite(clickedId)
            // view model의 favorites 를 최신화합니다.
            getFavorites()
        }

    }

    /** firebase에서 즐겨찾기 목록을 가져옵니다.*/
    suspend fun getFavorites() {
        firebaseRepository.getFavorites(_favorites)
    }

    /** 즐겨찾기 목록에 현재 station을 넣습니다. */
    private suspend fun addFavorite(stationId: Int) {
        firebaseRepository.postFavorites(stationId)
    }

    /** 즐겨찾기 목록에서 station을 삭제합니다. */
    private suspend fun deleteFavorite(stationId: Int) {
        firebaseRepository.deleteFavorite(stationId)
    }
}