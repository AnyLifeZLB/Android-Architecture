package com.architecture.demo.http.service

import com.architecture.demo.http.myinterface.AliApi
import com.architecture.demo.http.myinterface.FakerApi
import com.architecture.httplib.core.MoshiResultTypeAdapterFactory
import com.architecture.httplib.base.BaseHttpApiService
import okhttp3.Interceptor

/**
 * FakerApiService
 *
 */
object AliApiService : BaseHttpApiService("https://dashscope.aliyuncs.com/api/v1/") {

    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("Authorization", "Bearer sk-jFKisBOTle")
//            builder.addHeader("X-DashScope-WorkSpace", "application/json")
//            builder.addHeader("X-DashScope-SSE", "enable")

            chain.proceed(builder.build())
        }
    }


    /**
     * 再次封装一次，减少调用的代码量
     *
     */
    fun getService():AliApi{
        return getService(AliApi::class.java)
    }



    /**
     * 把服务器返回的JSON 通用的大体结构字段定义出来，方便解析
     *
     *     "status": "OK",
    "code": 200,
    "total": 15,
    "data": [
     *
     */
    override fun getHttpWrapperHandler(): MoshiResultTypeAdapterFactory.HttpWrapper{

        return object : MoshiResultTypeAdapterFactory.HttpWrapper {
            override fun getStatusCodeKey(): String {
                return "code"
            }

            override fun getErrorMsgKey(): String {
                return "message"
            }

            override fun getDataKey(): String {
                return "data"
            }

            // 这个Host 服务器对应200 表示成功
            override fun isRequestSuccess(statusCode: Int): Boolean {
                return statusCode == 200
            }
        }
    }

}

