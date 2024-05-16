package com.architecture.demo.http.myinterface

import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.httplib.core.HttpResult
import retrofit2.http.GET

/**
 * 可以比较简单的造出各种数据
 *
 */
interface FakerApi {

    // API 假如用不了不维护了。你可以自行替换可用的API https://fakerapi.it/en
    @GET("companies?_quantity=10")
    suspend fun getFakerData(): HttpResult<List<FakerDataBean>>



}