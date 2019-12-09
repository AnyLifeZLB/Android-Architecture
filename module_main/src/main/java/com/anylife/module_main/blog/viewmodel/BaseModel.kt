package com.anylife.module_main.blog.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by WG on 2019-12-06.
 * Email: wg5329@163.com
 * Github: https://github.com/WGwangguan
 * Desc:
 */
open class BaseModel : ViewModel() {
    val showLoading by lazy { MutableLiveData<Boolean>() }

    val showErrMsg by lazy { MutableLiveData<String>() }

    val showEmpty by lazy { MutableLiveData<String>() }

    open fun <T> parseResult(liveData: MutableLiveData<T>, resultData: ResultData<T>) {
        if (resultData.code == 200 && resultData.data != null) {
            showLoading.value = false
            liveData.value = resultData.data
        } else {
            showErrMsg.value = resultData.msg
        }
    }

}

data class ResultData<T>(
        val code: Int = 200,
        val msg: String = "",
        val data: T? = null
)