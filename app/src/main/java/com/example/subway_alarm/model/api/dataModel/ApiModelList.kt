package com.example.subway_alarm.model.api.dataModel

import com.google.gson.annotations.SerializedName

data class ApiModelList (
    @SerializedName("errorMessage")
    val errorMessage: ApiErrorMsg? = null,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<ApiModel>
)