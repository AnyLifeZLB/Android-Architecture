package com.anylife.keepalive.testhttp

import com.architecture.httplib.core.HttpResult
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 可以比较简单的造出各种数据
 *
 */
interface JsonApi {

    // API 假如用不了不维护了。你可以自行替换可用的API https://fakerapi.it/en
    @POST("TestData")
    suspend fun postJsonData(@Body l:JsonBean): HttpResult<Any>


}