package com.example.subway_alarm.viewModel.listener

interface OnSearchResultClick {
    /** 검색 결과를 클릭했을 때의 리스너입니다.*/
    fun onSearchResultClick(stationId: Int)
}