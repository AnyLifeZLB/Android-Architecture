package com.architecture.demo.ui.keepalive

import android.app.Application
import android.net.wifi.WifiManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architecture.demo.http.beans.FakerDataBean
import com.architecture.demo.http.beans.JsonBean
import com.architecture.demo.http.service.EuropeanaApiService
import com.architecture.demo.http.service.FakerApiService
import com.architecture.httplib.core.HttpResult
import com.architecture.httplib.utils.MoshiUtils
import com.architecture.demo.http.service.ExceptionApiService
import com.architecture.demo.http.service.JsonApiService
import com.architecture.demo.http.service.WanAndroidApiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

/**
 *
 *
 */
class KeepAliveViewModel : ViewModel() {


    fun postJsonData(mContext: Application) {

        viewModelScope.launch {
            val wifiService =
                mContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

            val json = JsonBean(
                Build.BRAND,
                stampToDate(System.currentTimeMillis()),
                wifiService.connectionInfo.ssid,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            )

            when (val result = JsonApiService.getService().postJsonData(json)) {
                is HttpResult.Success -> {
                    //List<FakerDataBean> 数据就直接出来了，方便极了
                    Log.e("KeepAlive", "success")
                }

                is HttpResult.Failure -> {
                    Log.e("KeepAlive", result.message + "  -----  " + result.code)
                }
            }

        }
    }


    /*
     * 将时间戳转换为时间
     *
     */
    private fun stampToDate(s: Long): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s)
        res = simpleDateFormat.format(date)
        return res
    }


}