package com.anylife.keepalive.testhttp

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.architecture.httplib.core.HttpResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 模拟后台持续收集数据，定位
 *
 */
class BackGroundWork {
    companion object {


        private fun stampToDate(s: Long): String {
            val res: String
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date(s)
            res = simpleDateFormat.format(date)
            return res
        }


        /**
         * 仅仅是验证，GlobalScope 谨慎使用
         *
         * @param mContext
         */
        fun postJsonData(mContext: Application) {

            GlobalScope.launch {

                val batteryManager =
                    mContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

                val wifiService =
                    mContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

                val json = JsonBean(
                    Build.BRAND + " -- 封装 Lib -- " + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
                    stampToDate(System.currentTimeMillis()),
                    wifiService.connectionInfo.ssid + " - " + wifiService.connectionInfo.bssid,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )

                when (val result = JsonApiService.getService().postJsonData(json)) {
                    is HttpResult.Success -> {
                        //List<FakerDataBean> 数据就直接出来了，方便极了
                        Log.e("KeepAlive", "success")
                    }

                    is HttpResult.Failure -> {
                        Log.e("KeepAlive", result.message + "  ---网络请求--  " + result.code)
                    }
                }

            }
        }
    }

}