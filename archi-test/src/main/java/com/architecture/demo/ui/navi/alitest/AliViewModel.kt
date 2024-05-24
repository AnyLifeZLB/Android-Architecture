package com.architecture.demo.ui.navi.alitest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architecture.baselib.livedata.StateLiveData
import com.architecture.demo.http.beans.FakerDataBean
import kotlinx.coroutines.launch


/**
 * ViewModel 没啥好说的
 *
 */
@Deprecated("临时使用")
class AliViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    private val repository by lazy { AliRepository() }

    val fakeDataList = StateLiveData<FakerDataBean>()


    fun requestNet() {
        viewModelScope.launch {
            repository.getFakerData(fakeDataList)
        }
    }


}