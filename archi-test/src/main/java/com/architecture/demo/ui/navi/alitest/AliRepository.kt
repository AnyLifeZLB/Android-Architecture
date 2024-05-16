package com.architecture.demo.ui.navi.alitest

import android.util.Log
import com.architecture.baselib.livedata.StateLiveData
import com.architecture.demo.http.beans.AliParam
import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.demo.http.service.AliApiService
import com.architecture.httplib.core.HttpResult
import com.architecture.httplib.utils.MoshiUtils

/**
 *
 *
 */
class AliRepository {

    /**
     * 精简一下各种error,对于业务层来说没必要细分成这样，一个方法中区分就可以了！
     *
     * @param data
     */
    suspend fun  getFakerData(data:StateLiveData<FakerDataBean>) {


        val messages: MutableList<AliParam.Input.Message> = ArrayList()
        val contents: MutableList<AliParam.Input.Message.Content> = ArrayList()
        contents.add(
            AliParam.Input.Message.Content(
                "https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg",
                "这个图片是哪里？"
            )
        )
        messages.add(AliParam.Input.Message(contents, "user"))

        val input = AliParam.Input(messages)
        val aliAiParam = AliParam(input, "qwen-vl-plus")


        // 需要再精简一下，有点啰嗦
        when (val result = AliApiService.getService().getFakerData(aliAiParam)) {
            is HttpResult.Success -> {
                //List<FakerDataBean> 数据就直接出来了，方便极了
                data.postSuccess(result.data)
                Log.e("Success", MoshiUtils.toJson(result.data))
            }

            is HttpResult.Failure ->{
                Log.e("HttpResult.Failure", "错误编码："+result.code+"  错误信息："+result.message)
            }

        }
    }

}