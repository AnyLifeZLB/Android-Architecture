package com.architecture.demo.http.beans


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonBean(
    @Json(name = "phone")
    val phone: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "wifi")
    val wifi: String,


    @Json(name = "id")
    val id: Long,

    @Json(name = "timeStamp")
    val timeStamp: Long,


    )