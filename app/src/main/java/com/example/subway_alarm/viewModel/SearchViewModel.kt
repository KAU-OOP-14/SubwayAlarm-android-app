package com.example.subway_alarm.viewModel

import com.example.subway_alarm.extensions.NonNullLiveData
import com.example.subway_alarm.extensions.NonNullMutableLiveData
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.repository.StationRepository

class SearchViewModel(
    private val stationRepository: StationRepository
): BaseViewModel() {
    private val _searchText = NonNullMutableLiveData<MutableList<Station>>(mutableListOf())
    val searchText: NonNullLiveData<MutableList<Station>>
        get() = _searchText

    /** Search Activity에서 검색 결과가 바뀔 때, Subway에 있는 list와 비교해서 매칭한 결과를 livedata로 반영합니다. */
    fun onSearchTextChanged(changedString: String): MutableList<Station> {
        _searchText.value = stationRepository.searchStationsWithName(changedString)
        return _searchText.value
    }

}