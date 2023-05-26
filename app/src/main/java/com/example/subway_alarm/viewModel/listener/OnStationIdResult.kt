package com.example.subway_alarm.viewModel.listener

interface OnStationIdResult {
    /** 특정 좌표를 검색해서 찾았을 때의 리스너 */
    fun onStationIdResult(staitonId: Int)
}