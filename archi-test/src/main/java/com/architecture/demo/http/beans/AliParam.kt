package com.architecture.demo.http.beans


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AliParam(
    @Json(name = "input")
    val input: Input,
    @Json(name = "model")
    val model: String
) {
    @JsonClass(generateAdapter = true)
    data class Input(
        @Json(name = "messages")
        val messages: List<Message>
    ) {
        @JsonClass(generateAdapter = true)
        data class Message(
            @Json(name = "content")
            val content: List<Content>,
            @Json(name = "role")
            val role: String
        ) {
            @JsonClass(generateAdapter = true)
            data class Content(
                @Json(name = "image")
                val image: String,
                @Json(name = "text")
                val text: String
            )
        }
    }
}