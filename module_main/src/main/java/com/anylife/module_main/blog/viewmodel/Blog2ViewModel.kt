package com.anylife.module_main.blog.viewmodel

import androidx.lifecycle.MutableLiveData
import com.anylife.module_main.blog.model.Blog2Repository
import com.zlb.persistence.entity.Blog

/**
 * Created by WG on 2019-12-06.
 * Email: wg5329@163.com
 * Github: https://github.com/WGwangguan
 * Desc:
 */
class Blog2ViewModel : BaseModel() {

    val blogLiveData by lazy { MutableLiveData<List<Blog>>() }

    fun loadData() {
        showLoading.value = true
//        Blog2Repository.getBlog {
//            if (it.code == "200" && it.data != null) {
//                showLoading.value = false
//                blogLiveData.value = it.data
//            } else {
//                showErrMsg.value = it.msg
//            }
//        }
        Blog2Repository.getBlog {
            parseResult(blogLiveData, it)
        }
    }
}