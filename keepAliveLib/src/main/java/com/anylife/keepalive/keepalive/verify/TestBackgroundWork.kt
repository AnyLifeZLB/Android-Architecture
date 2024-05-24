package com.anylife.keepalive.keepalive.verify

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.anylife.keepalive.keepalive.WorkTaskHelper
import java.text.SimpleDateFormat
import java.util.Date


/**
 * 仅仅用于临时验证，不是正式的代码
 *
 * @property context
 */
class TestBackgroundWork(private val context: Context) {


    /**
     * 时间戳转换为格式化时间
     * 仅仅用于临时验证
     *
     * @return
     */
    private fun stampToDate(s: Long): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s)
        res = simpleDateFormat.format(date)
        return res
    }


    /**
     * 验证在后台定位并上传日志数据到后台，目前的排序好像有点问题
     *
     */
    fun testDozeWork() {
        val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
        val wifiService = context.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

        val map: MutableMap<String, Any> = HashMap(6)
        map["phone"] = Build.BRAND + " - "+batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        map["time"] = stampToDate(System.currentTimeMillis())
        map["wifi"] = wifiService.connectionInfo.ssid+" - "+wifiService.connectionInfo.bssid
        map["timeStamp"] = System.currentTimeMillis()
        map["id"] = System.currentTimeMillis()


//        //自己搭建一个简易服务收集数据验证更简单好处理,先用项目中HTTP 请求，后期再用重新封装
//        HttpManager.RequestAsyncManager.requestPostMap("http://10.39.179.70:4389/TestData",
//            map,
//            Any::class.java,
//            object : HttpManager.HttpCallback {
//                override fun onSuccess(model: ResponseModel) {
//                    //请求成功 LOG
//                    Log.d(WorkTaskHelper.TAG, "keep alive 验证保活  &&&  onSuccess Http  ${Build.BRAND}")
//                }
//
//                override fun onError(model: ResponseModel) {
//                    //请求失败，LOG
//                    Log.w(WorkTaskHelper.TAG, "keep alive 验证保活s  ***  onError Http")
//                }
//            })
    }


}