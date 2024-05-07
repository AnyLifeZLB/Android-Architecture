package com.architecture.demo.http.myinterface

import com.architecture.demo.http.beans.AliParam
import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.httplib.core.HttpResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 可以比较简单的造出各种数据
 *
 */
interface AliApi {

    // API 假如用不了不维护了。你可以自行替换可用的API https://fakerapi.it/en
    @POST("services/aigc/multimodal-generation/generation")
    suspend fun getFakerData(@Body a: AliParam): HttpResult<FakerDataBean>



}