package com.example.subway_alarm.data.api.dataModel

import com.google.gson.annotations.SerializedName

data class ApiModelList (
    @SerializedName("errorMessage")
    val errorMessage: ApiErrorMsg? = null,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<ApiModel>? = null
)