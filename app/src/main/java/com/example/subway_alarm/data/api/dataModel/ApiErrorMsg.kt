package com.example.subway_alarm.data.api.dataModel

import com.google.gson.annotations.SerializedName

/**
 * api의 에러메세지에 해당하는 데이터입니다.
 */
data class ApiErrorMsg (
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("developerMessage")
    val developerMessage: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("total")
    val total: Int? = null
)