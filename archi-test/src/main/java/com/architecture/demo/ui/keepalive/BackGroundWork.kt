package com.architecture.demo.ui.keepalive

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.architecture.demo.http.beans.JsonBean
import com.architecture.demo.http.service.JsonApiService
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

                var scanResultStr=" - "


                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val scanResultList=wifiService.scanResults
                    for(s in scanResultList){
                        scanResultStr=scanResultStr+" "+s.SSID
                    }
                } else {
                    scanResultStr ="scan failed ******************* "
                }




                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val scanResultList=wifiService.scanResults
                    for(s in scanResultList){
                        scanResultStr=scanResultStr+" "+s.BSSID
                    }

                } else {
                    scanResultStr ="scan failed ******************* "
                }


                val json = JsonBean(
                    Build.BRAND + " -----         Service 外部     ----- " + batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY),
                    stampToDate(System.currentTimeMillis()),
                    wifiService.connectionInfo.ssid + "      *****    " + scanResultStr,
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
    }

}